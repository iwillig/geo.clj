(ns geo.proj
  (:import
   [org.geotools.geometry.jts JTS]
   [org.geotools.factory Hints]
   [org.geotools.referencing CRS ReferencingFactoryFinder]))

(def *crsfactory* (ReferencingFactoryFinder/getCRSFactory nil))

(defn proj-from-wkt
  [wkt]
  (. *crsfactory* createFromWKT  wkt))

(defn proj->epsg
  "Returns a epsg code from a CRS object"
  [crs]
     (System/setProperty "org.geotools.referencing.forceXY" "true")
     (Hints/putSystemDefault (Hints/COMPARISON_TOLERANCE) 1e-9)
     (format "EPSG:%s"(CRS/lookupEpsgCode crs true)))

(defn epsg->proj
  [epsg]
  (. CRS decode epsg))

(defn get-area
  [projection]
   (first (.getGeographicElements (.getDomainOfValidity projection))))

(defn transform
  [geometry input-epsg output-epsg]
  (. JTS transform geometry
     (. CRS findMathTransform
        (epsg->proj input-epsg)
        (epsg->proj output-epsg))))

