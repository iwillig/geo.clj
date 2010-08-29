(ns geo.io
  (:use
   [geo.seq]
   [geo.utils :only (java-apply)])
  (:import
   [java.io File]
   [org.geotools.filter.text.cql2  CQL]
   [org.geotools.feature.simple SimpleFeatureBuilder]
   [org.geotools.factory CommonFactoryFinder]
   [org.geotools.styling SLDParser]
   [org.geotools.data.memory MemoryFeatureCollection]
   [org.geotools.data DataStoreFinder DataStore]))

(defn read-properties
  [feature]
  (let [nongeom-properties (filter #(not (-> % .getValue class (isa? com.vividsolutions.jts.geom.Geometry)))
                                   (.getProperties feature))]
    (reduce (fn [hashmap field] (assoc hashmap (-> field .getDescriptor .getLocalName keyword)
                                       (.getValue field))) {} nongeom-properties)))

(def *style-factory*  (CommonFactoryFinder/getStyleFactory nil))

(defn read-sld
  [path]
  (first
   (.readXML (SLDParser. *style-factory* (-> path java.io.File. .toURL)))))

(defn geotoolsfeature->feature [feature]
 {:type "Feature"
  :id (.getID feature)
  :properties (read-properties feature)
  :geometry (.getDefaultGeometry feature)})

(defn feature->geotoolsfeature [feature feature-type]
  (let [props (:properties feature)
        feature-builder (SimpleFeatureBuilder. feature-type)]
    (doseq [prop-keyval (:properties feature)]
      (.set feature-builder (name (key prop-keyval)) (val prop-keyval)))
    (.set feature-builder  (.getLocalName (.getGeometryDescriptor feature-type)) (:geometry feature))
    (.buildFeature feature-builder (:id feature))))

(defmacro lazy-iterator-generator
  "macro to create an iterator or featureiterator with shared implementation"
  [fn-name iterator-type has-close]
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
            (if has-close
              (cons `(~'close [this]) impl)
              impl))))))

(lazy-iterator-generator
 make-lazy-feature-iterator
 org.geotools.feature.FeatureIterator
 true)

(lazy-iterator-generator
 make-lazy-iterator
 java.util.Iterator
 false)

(defn make-lazy-feature-collection
  "creates a minimal feature collection backed by the sequence which gets consumed lazily"
  [feature-builder feature-type sequence]
  (reify
   org.geotools.feature.FeatureCollection
   (getSchema [this] feature-type)
   (features [this]
             (make-lazy-feature-iterator feature-builder sequence))
   (iterator [this]
             (make-lazy-iterator feature-builder sequence))
   (^void close [this ^java.util.Iterator iterator] nil)))

(defn make-feature-builder
  "create a function that can take a feature hash and generate a geotools simplefeature"
  [feature-type]
  (let [feature-builder (SimpleFeatureBuilder. feature-type)]
    (fn [feature-hash]
      (doseq [prop-keyval (:properties feature-hash)]
        (.set feature-builder (name (key prop-keyval)) (val prop-keyval)))
      (.set feature-builder (.getLocalName (.getGeometryDescriptor feature-type)) (:geometry feature-hash))
      (.buildFeature feature-builder (:id feature-hash)))))

(defn make-feature-collection
  "creates a lazy implementation of a feature collection"
  [feature-type features]
  (make-lazy-feature-collection
   (make-feature-builder feature-type)
   feature-type
   features))

(defn read-features
  "FeatureCollection"
  [datastore & type-name]
  (map geotoolsfeature->feature
       (let [feature-source (java-apply datastore getFeatureSource type-name)]
         (iterator-seq (.. feature-source getFeatures iterator)))))

(defn get-feature-type [datastore & type-name]
  (java-apply datastore getSchema type-name))


(defn get-projection
  [datastore & type-name]
  (.. (java-apply datastore getFeatureSource type-name) getBounds crs))

(defn get-schema
  [feature-source]
  (seq (.getTypes (.getSchema feature-source))))

(defn make-datastore
  "convenience wrapper function to create a datastore from a mapping"
  [connection-info]
  (DataStoreFinder/getDataStore connection-info))

(defn make-filter
  [string]
  (CQL/toFilter string))

(defn make-feature-source
  "convenience wrapper function to create a feature source"
  ([conn-info]
     (.getFeatureSource (make-datastore conn-info)))
  ([conn-info & table]
     (.getFeatureSource (make-datastore conn-info) (first table))))

(defn read-shp
  "Reads and loads a shapefile"
  [path]
  (let [datastore (make-datastore {"url" (-> path java.io.File. .toURL)})]
    {:type "FeatureCollection"
     :features (read-features datastore)
     :projection (get-projection datastore)}))

(defn make-postgis
  ;; hack to make setting up a postgis database easier
  ;; should clean up with regex 
  [params]
  (let [conn (.split params "@")
        host (first (.split (second conn) ":"))
        port-table (second (.split (second conn) ":"))        
        port (first (.split port-table "/"))
        database  (second (.split port-table "/"))
        user (first (.split (first conn) ":"))
        password (second (.split (first conn) ":"))]
    (make-datastore {"dbtype" "postgis" "host" host  "database" database "port" port "user" user "passwd" password })))

(defn find-datastore
  ;; "shp://path" 
  ;; "pg://user:pass@localhost:port/db"
  ;; "h2://dbname"
  [params]
  (let [type (first (seq (.split params "://")))
        params (second (seq (.split params "://")))]
    (if (= type "shp") (make-datastore {"url" (-> params java.io.File. .toURL)})
        (if (= type "pg") (make-postgis params)
            (if (= type "h2") (make-datastore {"dbtype" "h2" "dbname" params}))))))


(defn read-pg
  "takes a postgis"
  [{:keys [dbtype database host port user passwd] :as connection-info} table-name]
  (let [datastore (make-datastore connection-info)]
    {:type "FeatureCollection"
     :features (read-features datastore table-name)
     :projection (get-projection datastore table-name)}))

(defn write-features
  "write a feature collection to a data store"
  [feature-collection datastore & type-name]
  (let [feature-source (java-apply datastore getFeatureSource type-name)]
    (.addFeatures feature-source feature-collection)))

;; writing to shapefile doesn't really work
;; it needs an existing shape file and it doesn't seem to currently complete
(defn write-shp
  "Takes a feature collection and writes it to an shapefile"
  [feature-collection path]
  (let [datastore (make-datastore {"url" (-> path java.io.File. .toURL)})]
    (write-features feature-collection datastore)))

(defn write-pg
  "takes a feature collections and writes it to an existing postgis table. returns the number of features added"
  [feature-collection connection-info table-name]
  (let [datastore (make-datastore connection-info)]
    (.size (write-features feature-collection datastore table-name))))
