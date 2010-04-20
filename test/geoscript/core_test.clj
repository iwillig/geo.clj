(ns geoscript.core-test
  (:use [geoscript.geometry] :reload-all)
  (:use [clojure.test]))

(deftest point
  (is (class (create-point 43 74)) "com.vividsolutions.jts.geom.Point" ))

(deftest linestring
  (is (class (create-line-string
              '([43 73] [34 89] [12 43]) )))
  "com.vividsolutions.jts.geom.LineString")
