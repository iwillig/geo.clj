(ns geoscript.io
  (:import
   [java.io File]
   [org.geotools.data DataStoreFinder]
   [org.geotools.data DataStore]))

(defn bounds
  [store]
  (def bound (. store getBounds ))
  {:maxx (. bound getMaxX) :maxy (. bound getMaxY)
   :minx (. bound getMinX) :miny (. bound getMinY)})

(defn buffer
  [store]
  (. store features))

(defn read-shapefile
  "Reads and loads a shapefile"
  [path]
  (. (. (. DataStoreFinder getDataStore 
           (doto (java.util.HashMap.)
             (.put "url" (.(File. path) toURL))))
        getFeatureSource )
     getFeatures
     ))


(defn write-shapefile
  "Takes a feature collection and writes it to a shapefile"
  [features name])
