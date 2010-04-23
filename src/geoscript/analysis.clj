(ns geoscript.analysis)


;; opertations on geometries
(defn buffer
  "creates a buffer"
  [geometry distance]
  (. geometry buffer distance))

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


