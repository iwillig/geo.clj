(ns geo.core
  (:gen-class :main true)  
  (:use geo.render geo.io)
  (:import org.geotools.data.DataStoreFinder)) 

(def *conn-info* {"dbtype" "postgis" "database" "newyork"                                                                                        
                  "host" "localhost" "port" "5432" "schema" "public"                                                                                           
                  "user" "postgres" "passwd" "password"}) 


(defn -main [& args] 
  (write-image "newyork.png" (.getFeatureSource (DataStoreFinder/getDataStore *conn-info*) "planet_osm_point" )))
