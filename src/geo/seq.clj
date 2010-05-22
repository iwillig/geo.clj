(ns geo.seq
  (:use [clojure.contrib.seq-utils :only (seq-on)]))

(defmethod seq-on ::coords [coll]
  (map #(vector (.x %) (.y %)) (.getCoordinates coll)))

(derive com.vividsolutions.jts.geom.MultiLineString ::coords)
(derive com.vividsolutions.jts.geom.LineString ::coords)
