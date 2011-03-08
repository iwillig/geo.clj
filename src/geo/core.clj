(ns geo.core
  (:gen-class :main true)
  (:use [geo io render]))



(defn -main [& args]
  (let [shape (data-store  "shp:///home/ivan/data/nybb.shp")]
    (process-features [f (-> shape (read-features))]
                      (assoc f :geometry (.buffer (:geometry f) 1000)))))
