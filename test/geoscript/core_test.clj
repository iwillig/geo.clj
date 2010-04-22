(ns geoscript.core-test
  (:use [geoscript.geometry] :reload-all)
  (:use [clojure.test]))

(deftest point
  (is (class (create-point 43 74)) "com.vividsolutions.jts.geom.Point" ))

(deftest linestring
  (is (class (create-line-string
              '((43 73) (34 89) (12 43)) )))
  "com.vividsolutions.jts.geom.LineString")

(deftest linear-ring
  (create-linear-ring '( (0 0) (0 1) (1 1) (1 0) (0 0)  ) ))

;;(deftest polygon
;;  (is (class (create-polygon '((0 0) (0 1) (1 1) (1 0) (0 0)) ))
;;      "com.vividsolutions.jts.geom.Polygon"))

(deftest well-know-text
  (is (class (from-wkt "LINESTRING(10 34,23 45)"))
      "com.vividsolutions.jts.geom.LineString"))
