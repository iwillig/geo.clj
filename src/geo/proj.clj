(ns geo.proj
  (:import
   [org.geotools.geometry.jts JTS]
   [org.geotools.referencing CRS ReferencingFactoryFinder]))

(def *crsfactory* (ReferencingFactoryFinder/getCRSFactory nil))

(defn proj-from-wkt
  [wkt]
  (. *crsfactory* createFromWKT  wkt))

(defn get-proj
  [epsg]
  (. CRS decode epsg))

(defn get-bounding
  [projection]
   (first (.getGeographicElements (.getDomainOfValidity projection))))

(defn transform
  [geometry input-epsg output-epsg]
  (. JTS transform (geometry)
     (. CRS findMathTransform
        (get-proj input-epsg)
        (get-proj output-epsg))))

