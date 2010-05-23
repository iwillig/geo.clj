(ns sld-sandbox
  (:use geo.io geo.render))

(defn -main [& args]
  (def shape-path "/Users/ivanwillig/Data/CENSUS/nycb2000_09c_av/nycb2000.shp")
  (def shape-con {"url" (-> shape-path java.io.File. .toURL)})
  (def store (make-datastore shape-con))
  (def collection (.getFeatureSource store (first (.getTypeNames store))))
  (def path "/Users/ivanwillig/dev/geo.clj/data/sample.sld")
  (swing collection path))

