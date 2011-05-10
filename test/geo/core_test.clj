(ns geo.core-test
  (:use
   [clojure.contrib.seq-utils :only (seq-on)]
   [geo.geometry :only(create-point
                       create-polygon
                       create-line-string
                       create-linear-ring
                       geometry->wkb
                       wkb->geometry
                       geometry->wkt
                       wkt->geometry)]
        :reload-all)
  (:use [clojure.test]))

(testing "Testing points"
  (testing "Testing point creatation"
    (is (= (class (create-point 43 74))
           com.vividsolutions.jts.geom.Point))
    (is (= (class (wkt->geometry "POINT (10 120)"))
           com.vividsolutions.jts.geom.Point)))
  (testing "Testing point well know text"
    (is (= (class (geometry->wkt (create-point 12 12))) String))))


(deftest linestring
  (is (class (create-line-string
              '((43 73) (34 89) (12 43)) )))
  "com.vividsolutions.jts.geom.LineString")

(deftest linear-ring
  (is (class (create-linear-ring '((0 0) (0 1) (1 1) (1 0) (0 0)) ))
      "com.vividsolutions.jts.geom.LinearRing"))

(deftest from-well-know-text
  (is (class (wkt->geometry "LINESTRING(10 34,23 45)"))
     "com.vividsolutions.jts.geom.LineString"))

(deftest to-well-know-text
  (is (class (geometry->wkt (create-point 10 10)))
      "java.lang.String"))

(deftest test-polygon
  (is (class (create-polygon '((0 0) (0 1) (1 1) (1 0) (0 0))))
      "com.vividsolutions.jts.geom.Polygon"))

(deftest test-seq-on-point
  (is (count (seq-on (create-point 10 10))) 3))
