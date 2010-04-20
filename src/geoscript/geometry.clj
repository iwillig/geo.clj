(ns geoscript.geometry
  (:import [org.geotools.geometry.jts JTSFactoryFinder]
           [com.vividsolutions.jts.geom Coordinate])) 

(def factory (JTSFactoryFinder/getGeometryFactory nil))

(defn valid? [geometry])

(defn create-coord
  "Creates a JTS Coordinate Seq"
  ([coord] (Coordinate. (first coord) (second coord)))
  ([x y](Coordinate. x y)))

(defn create-point
  "Creates a JTS Point from a X Y"
  [x y]
  (. factory createPoint (create-coord x y)))


(defn create-line-string
  "Creates a JTS LineString"
  [coords]
  (. factory createLineString (into-array (map create-coord coords))))

(defn create-polygon
  "Creates JTS Polygon"
  [shell, & holes])

(defn create-multi-point [])

(defn create-multi-line-string [])

(defn create-multi-polygon[])
