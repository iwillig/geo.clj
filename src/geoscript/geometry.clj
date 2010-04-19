(ns geoscript.geometry
  (:import [com.vividsolutions.jts.geom.GeometryFactory])) 

(def geom (GeometryFactory

(defn createPoint
  "Creates a JTS Point"
  [& x y ]
  (format "POINT %s" x))


(defn createLineString
  "Returns a JTS LineString"
  [])

(defn createPolygon [])

(defn createMultiPoint [])

(defn createMultiLineString [])

(defn createMultiPolygon[])
