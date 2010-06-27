(ns geo.core
  (:gen-class :main true)
  (:use geo.io geo.render)
 (:import org.geotools.data.DataStoreFinder))


(defn -main [& args]
 (def store (DataStoreFinder/getDataStore {"url" (-> "/home/ivan/Data/newyork/planet_osm_line.shp" java.io.File. .toURL)} ))
 (def features (.getFeatureSource store))
 (write-image "blah.png" features :style (read-sld "/home/ivan/Data/newyork/planet_line_test.sld")))
