(ns geoscript.io
  (:use
   [clojure.contrib.seq-utils :only (seq-on)])
  (:import
   [java.io File]
   [org.geotools.feature.simple SimpleFeatureBuilder]
   [org.geotools.data DataStoreFinder]
   [org.geotools.data DataStore]))


(defn dir
  "utility function for printing the methods of an object as strings"
  [object]
  (map #(.getName %) (.getMethods (class object))))


(defn make-feature
     "make-feature takes a geoscript clojure hashmap and
     produces a geotools feature, maybe
     feature TYPE"
     [map])

(defn read-properties
  [feature]
  (let [nongeom-properties (filter #(not (-> % .getValue class (isa? com.vividsolutions.jts.geom.Geometry)))
                                   (.getProperties feature))]
    (reduce (fn [map field] (assoc map (-> field .getDescriptor .getLocalName keyword)
                                   (.getValue field))) {} nongeom-properties)))

(defmethod seq-on com.vividsolutions.jts.geom.MultiLineString [multi-line-string]
  (map #(vector (.x %) (.y %)) (.getCoordinates multi-line-string)))

(defn read-features
  "FeatureCollection"
  [datastore & type-name]
  (map (fn [feature] {:type "Feature"
                      :properties (read-properties feature)
                      :geometry (.getDefaultGeometry feature)})
       (let [feature-source (if type-name
                              (.getFeatureSource datastore (first type-name))
                              (.getFeatureSource datastore))]
         (iterator-seq (.. feature-source getFeatures iterator)))))

(defn get-feature-type [datastore & type-name]
  (if type-name
    (.getSchema datastore (first type-name))
    (.getSchema datastore)))

(defn read-shapefile
  "Reads and loads a shapefile"  
  [path]
  {:type "FeatureCollection"
   :features (read-features
              (DataStoreFinder/getDataStore {"url" (-> path java.io.File. .toURL)}))})

(defn read-postgis
  "takes a postgis"
  [connection layer])


(defn write-shapefile
  "Takes a feature collection and writes it to a shapefile"
  [features name])
