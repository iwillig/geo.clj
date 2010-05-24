(ns sld-render
  (:use geo.io geo.render))

(defn -main [& args]
  (def shape-path "/home/ivan/Data/TM_WORLD_BORDERS-0.3.shp")
  (def shape-con {"url" (-> shape-path java.io.File. .toURL)})
  (def store (make-datastore shape-con))
  (def collection (.getFeatureSource store (first (.getTypeNames store))))
  (def path "/home/ivan/Data/polygon.sld")
  (defn show []
  (swing collection path)))

