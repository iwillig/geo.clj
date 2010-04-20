(ns geoscript.geometry
  (:import [org.geotools.geometry.jts JTSFactoryFinder]
           [com.vividsolutions.jts.geom Coordinate])) 

(def factory (JTSFactoryFinder/getGeometryFactory nil))

(defn create-coord
  "Creates a JTS Coordinate Seq"
  ([coord] (Coordinate. (apply coord) ))
  ([x y](Coordinate. x y)))

(defn create-point
  "Creates a JTS Point from a X Y"
  [x y]
  (. factory createPoint (create-coord x y)))


(defn create-line-string
  "Returns a JTS LineString"
  [& coords](map #(create-coord %) coords))

(defn makePolygon [])

(defn createMultiPoint [])

(defn createMultiLineString [])

(defn createMultiPolygon[])
