(ns geo.core
  (:gen-class :main true)
  (:use geo.io geo.render clojure.contrib.repl-utils)
  (:import [org.geotools.data.DataStoreFinder]
           [org.geotools.data Query]
           [org.geotools.jdbc VirtualTable]))



(defn -main [& args]
  (let [store (make-datastore { "dbtype" "postgis" "database"
                                "nyc" "host" "localhost" "port"
                                "5432" "user" "postgres"
                                "passwd" "password"})
        virtual (VirtualTable. "limit" "SELECT gid,name, the_geom FROM nyc_streets LIMIT 1000")]
      (.addGeometryMetadatata virtual "the_geom" com.vividsolutions.jts.geom.LineString 26918 )
      (.addVirtualTable store virtual) 
      (def limit (.getFeatureSource store "limit"))
      (def feature (.getFeatureSource store "nyc_streets"))))


;;(defn -main [& args]
;;  (def lines-store (DataStoreFinder/getDataStore {"url" (-> "/home/ivan/Data/newyork/planet_osm_line.shp" java.io.File. .toURL)} ))
;; (def lines (.getFeatureSource lines-store))
;; (write-image "images.png" (.getBounds lines) [{:feature (.getFeatureSource
;;                                                          (DataStoreFinder/getDataStore
;;                                                           {"url" (-> "/home/ivan/Data/newyork/planet_osm_line.shp" java.io.File. .toURL)}))
;;                                                :style (read-sld "/home/ivan/Data/newyork/planet_line_test.sld")}
;;                                               {:feature (.getFeatureSource
;;                                                          (DataStoreFinder/getDataStore
;;                                                         {"url" (-> "/home/ivan/Data/newyork/planet_osm_point.shp" java.io.File. .toURL)}))
;;                                                :style nil }]))
