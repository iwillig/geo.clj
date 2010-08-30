(ns geo.interface)

(defprotocol Collection
  "Abstraction representing a sequence of features"

  (features [this] "sequence of features implementing Feature")
  (feature-type [this] "geotools feature type of features")
  (projection [this] "crs projection")
  (bounds [this] "returns envelope bounding box of features")
  (close [this] "closes any resources that may be attached to collection"))

(defprotocol Feature
  "Abstraction representing a single feature"

  (id [this] "feature id")
  (properties [this] "hash of feature's properties")
  (geometry [this] "jts geometry object"))
