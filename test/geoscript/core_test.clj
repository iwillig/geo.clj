(ns geoscript.core-test
  (:use [geoscript.geometry :only(create-point
                                  create-polygon
                                  create-line-string
                                  create-linear-ring
                                  from-wkt)]
        [geoscript.analysis :only(buffer
                                  equals)]
        [geoscript.io :only(read-shapefile
                            bounds)]
        :reload-all)
  (:use [clojure.test]))

(deftest point
  (is (class (create-point 43 74)) "com.vividsolutions.jts.geom.Point" ))

(deftest linestring
  (is (class (create-line-string
              '((43 73) (34 89) (12 43)) )))
  "com.vividsolutions.jts.geom.LineString")

(deftest linear-ring
  (is (class (create-linear-ring '((0 0) (0 1) (1 1) (1 0) (0 0)) ))
      "com.vividsolutions.jts.geom.LinearRing"))

(deftest well-know-text
  (is (class (from-wkt "LINESTRING(10 34,23 45)"))
     "com.vividsolutions.jts.geom.LineString"))

(deftest test-equals
  (equals (create-point 43 63.2)(create-point 39 32)))

(deftest test-polygon
  (is (class ((create-polygon '((0 0) (0 1) (1 1) (1 0) (0 0))) :geometry))
      "com.vividsolutions.jts.geom.Polygon"))

(deftest shapefile
  (println  (read-shapefile "/home/ivan/Data/TM_WORLD_BORDERS-0.3.shp")))
