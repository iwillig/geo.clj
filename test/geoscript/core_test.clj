(ns geoscript.core-test
  (:use [geoscript.geometry] :reload-all)
  (:use [clojure.test]))

(deftest point
  (create-point 43 74))

(deftest linestring
  (create-line-string [(43 73) (34 89) (12 43)])) 
