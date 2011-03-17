(ns geo.core
  (:gen-class :main true)
  (:use [geo io render geometry]))

(def shape (data-store  "shp:///home/ivan/data/states.shp"))

;;; render shape using jcharts
(defn main []
  (with-features [f (-> shape (read-features))]
    (viewer (make-geometry-dataset
             [(.buffer (:geometry  (first
                                     (filter #(= "Texas"
                                                 (get-in % [:properties :STATE_NAME])) f))) 0.5) ]))))

