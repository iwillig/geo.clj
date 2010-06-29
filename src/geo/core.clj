(ns geo.core
  (:gen-class :main true)
  (:use geo.io geo.render)
 (:import org.geotools.data.DataStoreFinder))


(defn -main [& args]
 (def lines-store (DataStoreFinder/getDataStore {"url" (-> "/home/ivan/Data/newyork/planet_osm_line.shp" java.io.File. .toURL)} ))
 (def lines (.getFeatureSource lines-store))
 (def point-store (DataStoreFinder/getDataStore {"url" (-> "/home/ivan/Data/newyork/planet_osm_point.shp" java.io.File. .toURL)} ))
 (def points (.getFeatureSource point-store))
 (def lines-style (read-sld "/home/ivan/Data/newyork/planet_line_test.sld"))
 (write-image "images.png" (.getBounds lines) points))

