(ns geoscript.io
  (:import
   [java.io File]
   [org.geotools.feature.simple SimpleFeatureBuilder]
   [org.geotools.data DataStoreFinder]
   [org.geotools.data DataStore]))


(defn dir
  "utility function for print the methods of an object as strings"
  [object]
  (map (fn [x] (.getName x)) (.getDeclaredMethods (.getClass object))))


(defn bounds
  [store]
  (def bound (. store getBounds ))
  {:maxx (.getMaxX bound) :maxy (.getMaxY bound)
   :minx (.getMinX bound) :miny (.getMinY bound)})

(defn get-feature-type
  [feature]
  (.getFeatureType feature))

(defn get-properties
  [feature]
  (.getProperties feature)) 

(defn get-layer
  "FeatureCollection"
  [shape]
  (def feature (lazy-seq (.toArray (.(.(.getFeatureSource
                 shape (first (.getNames shape)))
                                       getFeatures) collection))))
  {:properties (map get-properties feature)})

(defn get-schema
  [store]
  (.getSchema store))

(defn read-shapefile
  "Reads and loads a shapefile"  
  [path]
  (def data (. DataStoreFinder getDataStore 
           (doto (java.util.HashMap.)
             (.put "url" (.(File. path) toURL)))))
  {:features (get-layer data)})


(defn read-postgis
  "takes a postgis"
  [connection layer])


(defn write-shapefile
  "Takes a feature collection and writes it to a shapefile"
  [features name])
