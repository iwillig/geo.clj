(ns geo.geometry
  (:use
   [clojure.contrib.seq-utils :only (seq-on)]
   [geo seq])
  (:require
   [clojure.contrib.json :as json])
  (:import [org.geotools.geometry.jts JTS JTSFactoryFinder]
           [org.geotools.referencing CRS]
           [javax.swing JFrame]
           [org.jfree.chart JFreeChart ChartPanel]
           [org.jfree.chart.plot XYPlot]
           [org.geotools.renderer.chart
            GeometryRenderer
            GeometryDataset]
           [com.vividsolutions.jts.geom
            Geometry
            MultiPoint
            MultiLineString
            MultiPolygon
            LineString
            Polygon
            LinearRing
            Point
            Coordinate])) 

(def *factory* (JTSFactoryFinder/getGeometryFactory nil))
(def *reader* (com.vividsolutions.jts.io.WKTReader. *factory*))

(defn write-geometry [geometry out]
  (.print out (json/json-str {:type (.getGeometryType geometry)
                               :coordinates [(seq-on geometry)]})))

(extend Point json/Write-JSON
        {:write-json write-geometry })

(extend LineString json/Write-JSON
        {:write-json write-geometry })

(extend Polygon json/Write-JSON
        {:write-json write-geometry })

(extend MultiPoint json/Write-JSON
        {:write-json write-geometry })

(extend MultiLineString json/Write-JSON
        {:write-json write-geometry })

(extend MultiPolygon json/Write-JSON
        {:write-json write-geometry})

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
  (.createMultiPoint *factory*
                     (into-array
                      (map #(create-point (first %) (second %)) points))))

(defn create-multi-line-string
  [lines]
  (.createMultiLineString *factory*
                          (into-array (map #(create-line-string %) lines ))))

(defn create-multi-polygon
  [polygons]
  (.createMultiPolygon *factory*
                       (into-array (map #(create-polygon %) polygons))))


(defn make-geometry-plot [geometies]
  (let [gd (GeometryDataset. (into-array Geometry geometies))
        render (GeometryRenderer.)]
    (XYPlot. gd (.getDomain gd) (.getRange gd) render)))

(defn view [geometies & {:keys [height width]
                         :or {height 500 width 500}}]
  (let [plot (make-geometry-plot geometies)
        chart (JFreeChart. plot)
        panel (ChartPanel. chart)]
    (doto (JFrame.) 
      (.setContentPane panel)
      (.setVisible true)
      (.setSize height width))))
