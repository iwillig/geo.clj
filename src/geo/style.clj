(ns geo.style
  (:import
   [org.geotools.styling SLDParser]
   [org.geotools.factory CommonFactoryFinder]))

(def *style-factory* (CommonFactoryFinder/getStyleFactory nil))

(defn read-sld
  [path]
  (first
   (.readXML (SLDParser. *style-factory* (-> path java.io.File. .toURL)))))
