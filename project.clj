(defproject geo "0.1"
  :description "a library for dealing with geotools in clojure"
  :repositories { "OpenGeo Maven Repository" "http://repo.opengeo.org/"}  
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.geotools/gt-main "2.7-SNAPSHOT"]
                 [org.geotools/gt-swing "2.7-SNAPSHOT"]
                 [org.geotools/gt-epsg-hsql "2.7-SNAPSHOT"]
                 [org.geotools/gt-referencing "2.7-SNAPSHOT"]
                 [org.geotools.jdbc/gt-jdbc-postgis "2.7-SNAPSHOT"]                 
                 [org.geotools.jdbc/gt-jdbc-h2 "2.7-SNAPSHOT"]                 
                 [org.geotools/gt-render "2.7-SNAPSHOT"]
                 [org.geotools/gt-shapefile "2.7-SNAPSHOT"]
                 [org.geotools/gt-swing "2.7-SNAPSHOT"]
                 [com.vividsolutions/jts "1.12-SNAPSHOT"]]
  :dev-dependencies [[swank-clojure "1.2.0"]])
