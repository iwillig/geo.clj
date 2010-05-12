(ns geoscript.core
  (:gen-class)
  (:use [geoscript.io :only(read-shapefile)]))

(defn -main [ & agrs]
  (println (map #(.isValid (% :geometry)) ((read-shapefile "/Users/ivanwillig/Data/CENSUS/nycb2000_09c_av/nycb2000.shp") :features))))

