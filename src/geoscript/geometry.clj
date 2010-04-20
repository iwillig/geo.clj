(ns geoscript.geometry
  (:import [org.geotools.geometry.jts JTSFactoryFinder]
           [com.vividsolutions.jts.geom Coordinate])) 

(def factory (JTSFactoryFinder/getGeometryFactory nil))

(defn createCoord
  "Creates a JTS Coordinate Seq"
  ([coord] (Coordinate. (coord) ))
  ([x y](Coordinate. x y)))

(defn createPoint
  "Creates a JTS Point from a X Y"
  [x y]
  (. factory createPoint (createCoord x y)))


(defn createLineString
  "Returns a JTS LineString"
  [& coords](map #(createCoord %) coords))

(defn makePolygon [])

(defn createMultiPoint [])

(defn createMultiLineString [])

(defn createMultiPolygon[])
