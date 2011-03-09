(ns geo.core
  (:gen-class :main true)
  (:use [geo io render geometry]))

(def shape (data-store  "shp:///home/ivan/data/states.shp"))

(defn -main [& args]
  (with-features [f (-> shape (read-features))]
    (viewer (make-geometry-dataset
             [(:geometry  (first
                            (filter #(= "New York"
                                        (get-in % [:properties :STATE_NAME])) f)))]))))
