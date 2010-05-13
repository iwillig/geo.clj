(ns geoscript.io
  (:import
   [java.io File]
   [org.geotools.feature.simple SimpleFeatureBuilder]
   [org.geotools.data DataStoreFinder]
   [org.geotools.data DataStore]))


(defn dir
  "utility function for printing the methods of an object as strings"
  [object]
  (map #(.getName %) (.getMethods (class object))))


(defn make-feature
     "make-feature takes a geoscript clojure hashmap and
     produces a geotools feature, maybe
     feature TYPE"
     [map])

(defn read-properties
  [feature]
  (reduce (fn [map field] (assoc map (-> field .getDescriptor .getLocalName keyword)
                               (.getValue field))) {} (rest (.getProperties feature))))

(defn read-features
  "FeatureCollection"
  [datastore]
  (map (fn [feature] {:type "Feature" :properties (read-properties feature) :geometry (.getDefaultGeometry feature)})
       ;; for shape file only
       (let [feature-source (.getFeatureSource datastore)
             features (.getFeatures feature-source)]
         (iterator-seq (.iterator features)))))

(defn read-shapefile
  "Reads and loads a shapefile"  
  [path]
  {:type "FeatureCollection"
   :features (read-features
              (DataStoreFinder/getDataStore {"url" (-> path java.io.File. .toURL)}))})

(defn read-postgis
  "takes a postgis"
  [connection layer])


(defn write-shapefile
  "Takes a feature collection and writes it to a shapefile"
  [features name])
