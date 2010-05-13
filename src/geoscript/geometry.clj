(ns geoscript.geometry
  (:use [clojure.contrib.json.write :only (json-str print-json)])
  (:import [org.geotools.geometry.jts JTS JTSFactoryFinder]
           [org.geotools.referencing CRS]
           [com.vividsolutions.jts.geom Coordinate])) 

(def factory (JTSFactoryFinder/getGeometryFactory nil))
(def reader (com.vividsolutions.jts.io.WKTReader. factory))

(defn valid?
  "checks the vaildity of a geometry"
  [geometry]
  (. geometry isValid))

(defn transform
  [geometry input-epsg output-epsg]
  (. JTS transform (geometry :geometry) (. CRS findMathTransform  (. CRS decode input-epsg) (. CRS decode output-epsg))))

(defn geojson-str
  [geometry]
  (json-str (map (fn [coord](vector (.x coord)(.y coord)))(.getCoordinates (geometry :geometry)))))

(defn read-geojson
  [string]) 

(defn from-wkt
  "Creates a geometry from well known text" 
  [string]
  (def point (. reader read (str string))) 
  {:type (.getGeometryType point) :geometry point})

(defn create-coord
  "Creates a JTS Coordinate Seq"
  ([coord] (Coordinate. (first coord) (second coord)))
  ([x y](Coordinate. x y)))

(defn create-point
  "Creates a JTS Point from a X Y"
  [x y]
  { :type "Point" :geometry (. factory createPoint (create-coord x y))})

(defn create-line-string
  "Creates a JTS Linear ring"
  [line]
  {:type "LineString" :geometry
   (. factory createLineString (into-array (map create-coord line)))})

(defn create-linear-ring
  "Creates a JTS Linear ring"
  [ring]
  {:type "LinearRing" :geometry
  (. factory createLinearRing (into-array (map create-coord ring)))})

(defn create-polygon
  "Creates JTS Polygon"
  ([shell]
  {:type "Polygon" :geometry
  (. factory createPolygon ((create-linear-ring shell) :geometry) nil)})
  ([shell & holes]
     {:type "Polygon" :geometry
      (. factory createPolygon ((create-linear-ring shell) :geometry)
         (into-array (map (create-linear-ring :geometry)  holes)))}))

(defn create-multi-point  
  [points])

(defn create-multi-line-string [])

(defn create-multi-polygon[])
