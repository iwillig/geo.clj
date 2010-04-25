(ns geoscript.analysis
  (:import [com.vividsolutions.jts.geom Geometry]))


(defn buffer-dispatch [geom & args] (class geom))
(defmulti buffer buffer-dispatch)
(defmethod buffer com.vividsolutions.jts.geom.Geometry geom
  [geom & args]
  (.buffer geom (first args)))


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


