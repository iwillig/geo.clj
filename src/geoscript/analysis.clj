(ns geoscript.analysis
  (:import [com.vividsolutions.jts.geom Geometry]))


(defn buffer-dispatch [collection & args] (class collection))

(defmulti buffer buffer-dispatch)

(defmethod buffer com.vividsolutions.jts.geom.Geometry collection
  [collection & args]
  (.buffer collection (first args)))

(defmethod buffer org.geotools.feature.FeatureCollection collection
  [collection & args]
  (map (fn [feature] (buffer (.getDefaultGeometry feature) (first args))) (.toArray collection)))

(defn equals
  "spatially equal to: a=b"
  [ageom bgeom]
  (. ageom equals bgeom))

(defn disjoint
  "spatial disjoint"
  [& geometries])

(defn intersects
  "spatially intersect"
  [& geometries])


