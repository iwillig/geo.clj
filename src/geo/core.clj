(ns geo.core
  (:gen-class :main true)
  (:use [geo io render geometry proj]))

(def shapes (data-store  "shp:///home/ivan/data/co-gen-enegry/"))

(def *db* (data-store "pg://postgres:password@localhost:5432/co_gen"))

(def nybb (data-store "shp:///home/ivan/data/nybb.shp"))

(def buildings (-> shapes
                   (read-features :layer "DOITT_BUILDING_01_13SEPT2010")))

(defn main [])

