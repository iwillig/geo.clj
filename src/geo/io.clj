(ns geo.io
  (:use
   [clojure.contrib.seq-utils :only (seq-on)])
  (:import
   [java.io File]
   [org.geotools.feature.simple SimpleFeatureBuilder]
   [org.geotools.data.memory MemoryFeatureCollection]
   [org.geotools.data DataStoreFinder DataStore]))

(defn dir
  "utility function for printing the methods of an object as strings"
  [object]
  (map #(.getName %) (.getMethods (class object))))

(defmacro java-apply [instance method args]
  `(clojure.lang.Reflector/invokeInstanceMethod
    ~instance (name (quote ~method)) (to-array ~args)))

(defn read-properties
  [feature]
  (let [nongeom-properties (filter #(not (-> % .getValue class (isa? com.vividsolutions.jts.geom.Geometry)))
                                   (.getProperties feature))]
    (reduce (fn [hashmap field] (assoc hashmap (-> field .getDescriptor .getLocalName keyword)
                                       (.getValue field))) {} nongeom-properties)))

(defmethod seq-on com.vividsolutions.jts.geom.MultiLineString [multi-line-string]
  (map #(vector (.x %) (.y %)) (.getCoordinates multi-line-string)))

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
    ;; hard coded geom
    (.set feature-builder "geom" (:geometry feature))
    (.buildFeature feature-builder (:id feature))))

(defn make-feature-collection [feature-type features]
  (let [feature-builder (SimpleFeatureBuilder. feature-type)
        memory-feature-collection (MemoryFeatureCollection. feature-type)]
    (doseq [feature features]
      (doseq [prop-keyval (:properties feature)]
        (.set feature-builder (name (key prop-keyval)) (val prop-keyval)))
      ;; hard coded geom
      (.set feature-builder "geom" (:geometry feature))
      (.add memory-feature-collection
            (.buildFeature feature-builder (:id feature))))
    memory-feature-collection))

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

(defn make-datastore
  "convenience wrapper function to create a datastore from a mapping"
  [connection-info]
  (DataStoreFinder/getDataStore connection-info))

(defn read-shapefile
  "Reads and loads a shapefile"
  [path]
  (let [datastore (make-datastore {"url" (-> path java.io.File. .toURL)})]
    {:type "FeatureCollection"
     :features (read-features datastore)
     :projection (get-projection datastore)}))

(defn read-postgis
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
(defn write-shapefile
  "Takes a feature collection and writes it to an shapefile"
  [feature-collection path]
  (let [datastore (make-datastore {"url" (-> path java.io.File. .toURL)})]
    (write-features feature-collection datastore)))

(defn write-postgis
  "takes a feature collections and writes it to an existing postgis table. returns the number of features added"
  [feature-collection connection-info table-name]
  (let [datastore (make-datastore connection-info)]
    (.size (write-features feature-collection datastore table-name))))