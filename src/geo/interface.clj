(ns geo.interface)

(defprotocol Collection
  "Abstraction representing a sequence of features"

  (features [this] "sequence of features implementing GeoFeature")
  (feature-type [this] "geotools feature type of features")
  (projection [this] "crs projection")
  (bounds [this] "return extent or bounding box of the features")
  (close [this] "closes any resources that may be attached to collection"))

(defprotocol Feature
  "Abstraction representing a single feature"

  (id [this] "feature id")
  (properties [this] "hash of feature's properties")
  (geometry [this] "jts geometry object"))
