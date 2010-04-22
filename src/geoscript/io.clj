(ns geoscript.io
  (:import [org.geotools.data DataStore]))

(defn write-json
  "Takes a geometry or a feature collection and creates a geojson"
  [feature])

(defn read-shapefile
  "Reads and loads a shapefile into"
  [path])

(defn write-shapefile
  "Takes a feature collection and writes it to a shapefile"
  [features name])
