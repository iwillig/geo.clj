(ns geoscript.geometry
  (:import [org.geotools.geometry.jts JTSFactoryFinder]
           [com.vividsolutions.jts.geom Coordinate])) 

(def factory (JTSFactoryFinder/getGeometryFactory nil))
(def reader (com.vividsolutions.jts.io.WKTReader. factory))

(defn valid?
  "checks the vaildity of a geometry"
  [geometry]
  (. geometry isValid))



(defn from-wkt
  "Creates a geometry from well known text" 
  [string]
  (. reader read (str string))) 

(defn create-coord
  "Creates a JTS Coordinate Seq"
  ([coord] (Coordinate. (first coord) (second coord)))
  ([x y](Coordinate. x y)))


(defn create-point
  "Creates a JTS Point from a X Y"
  [x y]
  (. factory createPoint (create-coord x y)))
;;  { :x :y :geom (. factory createPoint (create-coord x y))} )


(defn create-line-string
  "Creates a JTS Linear ring"
  [input]
  (. factory createLineString (into-array (map create-coord input))))



(defn create-linear-ring
  "Creates a JTS Linear ring"
  [ring]
  (. factory createLinearRing (into-array (map create-coord ring))))


(defn create-polygon
  "Creates JTS Polygon"
  ([shell, & holes]
     (. factory createPolyon (create-linear-ring shell) (into-array holes))))

(defn create-multi-point [])

(defn create-multi-line-string [])

(defn create-multi-polygon[])
