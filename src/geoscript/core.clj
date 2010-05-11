(ns geoscript.core
  (:gen-class)
  (:use [geoscript.io :only(read-shapefile)]))

(defn -main [ & agrs]
  (println (map #( % :properties)
                ((read-shapefile "/home/ivan/Data/TM_WORLD_BORDERS-0.3.shp"):features))))

