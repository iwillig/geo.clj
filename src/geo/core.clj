(ns geo.core
  (:gen-class :main true)
  (:import [org.geotools.data DataUtilities]
           [java.io File])
  (:require [clojure.contrib.string :as string])
  (:use [geo.io :only (data-store
                       create-shapefile
                       process-features
                       read-features
                       write-features)]
        [geo.geometry :only (create-point)]))

(defn main [& args]
  (let [output    (create-shapefile (File. "/home/ivan/Desktop/noed_fixed.shp"))        
        offset-x  (- 513448.9865796  179739.79)
        offset-y  (- 9757443.3947178  9756924.24)
        schema (DataUtilities/createType
                "noed_fixed"
                (string/join ","
                             ["the_geom:Point:srid=32736"
                              "ID:Integer"
                              "DC_ID:String"
                              "ELEVATION:Double"
                              "DEMAND:Double"
                              "PATTERN:String"
                              "RESULT_HEA:Double"
                              "RESULT_PRE:Double"
                              "RESULT_DEM:Double"
                              "EMITTERCOE:Double"
                              "IDENTIFICA:String"
                              "LOCATION:String"]))]
    (.createSchema output schema)
    (write-features
     output
     "noed_fixed"
     (process-features
      [f (-> (data-store "shp:////home/ivan/Downloads/noed.shp") (read-features))]
      (assoc  f :geometry (create-point (- (.getX (:geometry f)) offset-x)
                                        (- (.getY (:geometry f) ) offset-y)))))))
