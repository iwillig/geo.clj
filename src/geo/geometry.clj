(ns geo.geometry
  (:use [clojure.contrib.json.write :only (json-str print-json)])
  (:import [org.geotools.geometry.jts JTS JTSFactoryFinder]
           [org.geotools.referencing CRS]
           [com.vividsolutions.jts.geom Coordinate])) 

(def *factory* (JTSFactoryFinder/getGeometryFactory nil))
(def *reader* (com.vividsolutions.jts.io.WKTReader. *factory*))

(defn valid?
  "checks the vaildity of a geometry"
  [geometry]
  (. geometry isValid))

(defn union
  [geom plusgeom]
  (.union geom plusgeom))

(defn get-coords
  [geometry]
  (seq (.getCoordinates geometry)))

(defn geojson-str
  [geometry]
  (json-str (map (fn [coord](vector (.x coord)(.y coord)))(.getCoordinates geometry))))

(defn read-geojson
  [string]) 

(defn from-wkt
  "Creates a geometry from well known text" 
  [string]
  (.read *reader* (str string)))

(defn create-coord
  "Creates a JTS Coordinate Seq"
  ([coords] (apply create-coord coords))
  ([x y] (Coordinate. x y))
  ([x y z] (Coordinate. x y z)))

(defn create-point
  "Creates a JTS Point from a X Y"
  ([p] (apply create-point p))
  ([x y] (.createPoint *factory* (create-coord x y)))
  ([x y z] (.createPoint *factory* (create-coord x y z))))

(defn create-line-string
  "Creates a JTS Linear ring"
  [line]
  (.createLineString *factory* (into-array (map create-coord line))))

(defn create-linear-ring
  "Creates a JTS Linear ring"
  [ring]
  (.createLinearRing *factory* (into-array (map create-coord ring))))

(defn create-polygon
  "Creates JTS Polygon"
  ([shell & holes]
     (.createPolygon
      *factory*
      (create-linear-ring shell)
      (and holes (into-array (map create-linear-ring holes))))))

(defn create-multi-point
  [points]
  (.createMultiPoint *factory* (into-array (map #(create-point (first %) (second %)) points))))

(defn create-multi-line-string
  [lines]
  (.createMultiLineString *factory* (into-array (map #(create-line-string %) lines ))))

(defn create-multi-polygon
  [polygons]
  (.createMultiPolygon *factory* (into-array (map #(create-polygon %) polygons))))
