(defproject geoscript "1.0.0-SNAPSHOT"
  :description "a library for dealing with geotools in clojure"
  :repositories { "OpenGeo Maven Repository" "http://repo.opengeo.org/"}  
  :dependencies [[org.clojure/clojure "1.1.0"]
                 [org.geotools/gt-main "2.7-SNAPSHOT"]
                 [org.clojure/clojure-contrib "1.1.0"]]
  :dev-dependencies [[swank-clojure "1.1.0"]]
  :main geoscript.geometry
  )
