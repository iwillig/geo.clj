(ns geoscript.io
  (:import
   [java.io File]
   [org.geotools.data DataStoreFinder]
   [org.geotools.data DataStore]))


(defn read-shapefile
  "Reads and loads a shapefile"
  [path]
  (. (. DataStoreFinder getDataStore 
  (doto (java.util.HashMap.)(.put "url" (.(File. path) toURL)))) getFeatureSource))


(defn write-shapefile
  "Takes a feature collection and writes it to a shapefile"
  [features name])
