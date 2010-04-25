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

(defn get-features
  "FeatureCollection"
  [collection]
  (.(.(.getFeatureSource collection (first (.getNames collection))) getFeatures) collection))

(defn read-shapefile
  "Reads and loads a shapefile"  
  [path]
  (. DataStoreFinder getDataStore 
           (doto (java.util.HashMap.)
             (.put "url" (.(File. path) toURL)))))


(defn read-postgis
  "takes a postgis"
  [connection layer])


(defn write-shapefile
  "Takes a feature collection and writes it to a shapefile"
  [features name])
