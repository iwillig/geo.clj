(ns geoscript.io
  (:import
   [java.io File]
   [org.geotools.data DataStoreFinder]
   [org.geotools.data DataStore]))

(defn bounds
  [store]
  (def bound (. store getBounds ))
  {:maxx (.getMaxX bound) :maxy (.getMaxY bound)
   :minx (.getMinX bound) :miny (.getMinY bound)})

(defn buffer
  [store]
  (. store features))

(defn get-features
  "FeatureCollection"
  [collection]
  (seq (.toArray collection)))

(defn read-shapefile
  "Reads and loads a shapefile"  
  [path]
  (def shape (. DataStoreFinder getDataStore 
           (doto (java.util.HashMap.)
             (.put "url" (.(File. path) toURL)))))
  (.(.(.getFeatureSource shape (first (.getNames shape))) getFeatures) collection))




(defn write-shapefile
  "Takes a feature collection and writes it to a shapefile"
  [features name])
