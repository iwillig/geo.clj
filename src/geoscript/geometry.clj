(ns geoscript.geometry
  (:import [org.geotools.geometry.jts JTSFactoryFinder]
           [com.vividsolutions.jts.geom Coordinate])) 

(def factory (JTSFactoryFinder/getGeometryFactory nil))

(defn makeCoord
  "Creates a JTS Coordinate Seq"
  [x y]
  (Coordinate. x y))

(defn makePoint
  "Creates a JTS Point from a X Y"
  [x y]
  (. factory createPoint (makeCoord x y)))


(defn makeLineString
  "Returns a JTS LineString"
  [])

(defn makePolygon [])

(defn createMultiPoint [])

(defn createMultiLineString [])

(defn createMultiPolygon[])
