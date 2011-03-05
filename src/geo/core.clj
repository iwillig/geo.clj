(ns geo.core
  (:gen-class :main true)
  (:use geo.io geo.render))



(defn -main [& args]
  (let [shape
        (data-store  "shp:///home/ivan/Desktop/MapPluto/NYCBldgs_2007.shp")]))
