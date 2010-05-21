(defproject geo "1.0.0-SNAPSHOT"
  :description "a library for dealing with geotools in clojure"
  :repositories { "OpenGeo Maven Repository" "http://repo.opengeo.org/"}  
  :dependencies [[org.clojure/clojure "1.1.0"]
                 [lein-run "1.0.0-SNAPSHOT"]
                 [org.geotools/gt-main "2.7-SNAPSHOT"]
                 [org.geotools/gt-swing "2.7-SNAPSHOT"]
                 [org.geotools/gt-epsg-hsql "2.7-SNAPSHOT"]
                 [org.geotools/gt-referencing "2.7-SNAPSHOT"]
                 [org.geotools/gt-postgis "2.7-SNAPSHOT"]
                 [org.geotools/gt-render "2.7-SNAPSHOT"]
                 [org.geotools/gt-shapefile "2.7-SNAPSHOT"]
                 [org.geotools/gt-swing "2.7-SNAPSHOT"]
                 [org.clojure/clojure-contrib "1.1.0"]]
  :dev-dependencies [[swank-clojure "1.1.0"]]
  :main geo.geometry
  :namespace [[geo.geometry]])
