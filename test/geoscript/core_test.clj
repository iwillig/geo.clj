(ns geoscript.core-test
  (:use [geoscript.geometry] :reload-all)
  (:use [clojure.test]))

(deftest point
  (createPoint 43 74))

(deftest linestring
  (createLineString [(43 73) (34 89) (12 43)])) 
