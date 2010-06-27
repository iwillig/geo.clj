(ns geo.core
  (:gen-class :main true)  
  (:use geo.render geo.io)
  (:import org.geotools.data.DataStoreFinder)) 

(def *conn-info* {"dbtype" "postgis" "database" "newyork"                                                                                        
                  "host" "localhost" "port" "5432" "schema" "public"                                                                                           
                  "user" "postgres" "passwd" "password"}) 


(defn show-map [& args]
  (def path "/Users/ivanwillig/Data/OSM/Shapes/osm_line.shp")
  (def data (make-datastore {"url" (-> path java.io.File. .toURL)}))
  (write-image "osm_line.png" (.getFeatureSource data)))
