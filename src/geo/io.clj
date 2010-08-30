(ns geo.io
  (:use
   [geo.utils :only (java-apply)])
  (:require
   [geo.interface :as geo])
  (:import
   [java.io File]
   [com.vividsolutions.jts.geom Geometry]
   [org.geotools.data DefaultTransaction]
   [org.geotools.feature.simple SimpleFeatureBuilder]
   [org.geotools.data.memory MemoryFeatureCollection]
   [org.geotools.data DataStoreFinder]))

(defn data-store
  ;; "shp://path"
  ;; "pg://user:pass@localhost:port/db"
  ;; "h2://dbname
  "returns a geotools datastore given an input string uri"
  [uri]
  (when-let
      [params
       (condp #(.startsWith %2 %) uri
         "shp://" {"url"
                   (-> (.substring uri (count "shp://"))
                       File. .toURL)}
         "pg://" (when-let
                     [[_ user pass host port db]
                      (re-find
                       (re-pattern "pg://([^:]*):([^@]*)@([^:]*):([^/]*)/(.*)")
                       uri)]
                   {"dbtype" "postgis" "host" host "port" port
                    "user" user "passwd" pass "database" db})
         "h2://" {"dbtype" "h2"
                  "dbname" (.substring uri (count "h2://"))})]
    (DataStoreFinder/getDataStore params)))

(defn read-properties
  "given a geotools feature, return a hash of properties that aren't geometries"
  [geotools-feature]
  (let [geom-prop? #(-> % .getValue class (isa? Geometry))
        nongeom-properties (remove geom-prop?
                                   (.getProperties geotools-feature))]
    (reduce (fn [hashmap field]
              (assoc hashmap
                (-> field .getDescriptor .getLocalName keyword)
                (.getValue field)))
            {}
            nongeom-properties)))

(defn make-feature [geotools-feature]
  "given a geotools feature, adapt to our custom type"
  (reify
   geo/Feature
   (id [this] (.getID geotools-feature))
   (properties [this] (read-properties geotools-feature))
   (geometry [this] (.getDefaultGeometry geotools-feature))))

(defmacro lazy-feature-iterator-generator
  "macro to create an iterator or featureiterator"
  [fn-name iterator-type]
  `(defn ~fn-name [~'feature-builder ~'feature-sequence]
     (let [~'features-state (atom ~'feature-sequence)]
       (reify
        ~iterator-type
        ~@(let [impl
                `((~'hasNext [this] (boolean (seq @~'features-state)))
                  (~'next [this]
                          (let [current-state# @~'features-state]
                            (swap! ~'features-state next)
                            (~'feature-builder (first current-state#)))))]
            (if (= iterator-type org.geotools.feature.FeatureIterator)
              (cons `(~'close [this]) impl)
              impl))))))

(lazy-feature-iterator-generator
 make-lazy-feature-iterator
 org.geotools.feature.FeatureIterator)

(lazy-feature-iterator-generator
 make-lazy-iterator
 java.util.Iterator)

(defn make-feature-builder
  "create a function that can take a clojure feature
   and generates a geotools feature"
  [feature-type]
  (let [feature-builder (SimpleFeatureBuilder. feature-type)]
    (fn [clj-feature]
      (doseq [prop-keyval (geo/properties clj-feature)]
        (.set feature-builder (name (key prop-keyval)) (val prop-keyval)))
      (.set feature-builder
            (.getLocalName (.getGeometryDescriptor feature-type))
            (geo/geometry clj-feature))
      (.buildFeature feature-builder (geo/id clj-feature)))))

(defn make-lazy-feature-collection
  "creates a lazy implementation of a feature collection"
  [feature-type features-sequence]
  (let [feature-builder (make-feature-builder feature-type)]
    (reify
     org.geotools.feature.FeatureCollection
     (getSchema [this] feature-type)
     (features [this]
               (make-lazy-feature-iterator feature-builder features-sequence))
     (iterator [this]
               (make-lazy-iterator feature-builder features-sequence))
     (^void close [this ^java.util.Iterator iterator] nil))))

(defn make-eager-feature-collection
  "uses the geotools memoryfeaturecollection to store all features in memory"
  [geo-sequence]
  (let [feature-type (.feature-type geo-sequence)
        feature-builder (make-feature-builder feature-type)
        memory-feature-collection (MemoryFeatureCollection. feature-type)]
    (doseq [clj-feature (.features  geo-sequence)]
      (.add memory-feature-collection (feature-builder clj-feature)))
    memory-feature-collection))

(defn read-features
  "read features from data store and return a collection"
  [datastore & type-name]
  (let [feature-source (java-apply datastore getFeatureSource type-name)
        feature-collection (.. feature-source getFeatures)
        feature-iterator (.iterator feature-collection)]
    (reify
     geo/Collection
     (features [this] (map make-feature (iterator-seq feature-iterator)))
     (feature-type [this] (java-apply datastore getSchema type-name))
     (projection [this]
                 (.. (java-apply datastore getFeatureSource type-name)
                     getBounds crs))
     (bounds [this] (.getBounds feature-collection))
     (close [this] (.close feature-collection feature-iterator)))))

(defn layers
  "nicer output for the repl"
  [datastore]
  (seq (.getTypeNames datastore)))

(defn add-layer!
  "creates a layer from an given schema"
  [datastore schema]
  (.createSchema datastore schema))

(defn write-feature
  "writes a collection to an existing layer in a datastore"
  [datastore geo-collection]
  (let [transaction (DefaultTransaction. "add")
        gt-collection (make-lazy-feature-collection
                    (.feature-type geo-collection)
                    (.features geo-collection))
        feature-source (.getFeatureSource datastore
                         (.getLocalPart (.getName (.feature-type geo-collection))))]
    (.setTransaction feature-source transaction)
    (try
      (.addFeatures feature-source gt-collection)
      (.commit transaction)
      (println "success")
      (catch Exception _ (.rollback transaction ))
      (finally (.close transaction)))))

