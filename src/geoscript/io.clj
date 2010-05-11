(ns geoscript.io
  (:import
   [java.io File]
   [org.geotools.feature.simple SimpleFeatureBuilder]
   [org.geotools.data DataStoreFinder]
   [org.geotools.data DataStore]))


(defn dir
  "utility function for print the methods of an object as strings"
  [object
  (map (fn [x] (.getName x)) (.getDeclaredMethods (.getClass object))))


(defn bounds
  [store]
  (def bound (. store getBounds ))
  {:maxx (.getMaxX bound) :maxy (.getMaxY bound)
   :minx (.getMinX bound) :miny (.getMinY bound)})

(defn get-feature-type
  [feature]
  (.getFeatureType feature))

(defn make-properties
  [feature]
  (map (fn [field] {(keyword (.getLocalName (.getDescriptor field))) (.getValue field)}) (rest (.getProperties feature))))

(defn make-features
  "FeatureCollection"
  [shape]  
  (map (fn [feature] {:type "Feature" :properties (make-properties feature) :geometry (.getDefaultGeometry feature)})
       (.toArray (.(.(.getFeatureSource
                 shape (first (.getNames shape)))
                          getFeatures) collection))))
  
(defn get-schema
  [store]
  (.getSchema store))

(defn read-shapefile
  "Reads and loads a shapefile"  
  [path]
  {:type "FeatureCollection" :features (make-features (. DataStoreFinder getDataStore 
           (doto (java.util.HashMap.)
             (.put "url" (.(File. path) toURL)))))})


(defn read-postgis
  "takes a postgis"
  [connection layer])


(defn write-shapefile
  "Takes a feature collection and writes it to a shapefile"
  [features name])
