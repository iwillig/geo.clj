(ns geo.render
  (:import [org.geotools.data FeatureSource]
           [javax.imageio ImageIO]
           [org.geotools.map DefaultMapContext MapContext]
           [org.geotools.styling SLDParser]
           [org.geotools.factory CommonFactoryFinder]
           [javax.swing JFrame]
           [org.geotools.swing JMapFrame]))

(def *style-factory*  (CommonFactoryFinder/getStyleFactory nil))

(defn make-style
  [path]
  (first
   (.readXML (SLDParser. *style-factory* (-> path java.io.File. .toURL)))))

(defn make-mapcontext
  "builds a DefaultMapContext
    Options can be:
      :title \"Title of JFrame\"
      :style geotools-style-object"
  [feature-collection & mapoptions]
  (let [options (apply hash-map mapoptions)]
    (doto (DefaultMapContext.)
      (.setTitle (or (:title options) "Default Map"))
      (.addLayer feature-collection (:style options)))))

(defn swing
  "create a swing frame displaying the features in the geotools
   featurecollection. See make-mapcontext for options"
  [feature-collection & frameoptions]
  (doto (JMapFrame. (apply make-mapcontext feature-collection frameoptions))
      (.setDefaultCloseOperation (JFrame/DISPOSE_ON_CLOSE))
      (.setSize 800 600)
      (.enableStatusBar true)
      (.enableToolBar true)
      (.setVisible true)))

(defn render-image
  "renders a images"
  [feature-collection sld]
  (make-mapcontext feature-collection sld))
