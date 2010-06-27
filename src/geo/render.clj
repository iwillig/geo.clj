(ns geo.render
  (:import [org.geotools.data FeatureSource]
           [javax.imageio ImageIO]
           [org.geotools.map DefaultMapContext MapContext]
           [org.geotools.styling SLDParser]
           [org.geotools.factory CommonFactoryFinder]
           [javax.swing JFrame]
           [java.io File]
           [org.geotools.renderer.lite StreamingRenderer]
           [java.awt Rectangle]
           [java.awt.image BufferedImage] 
           [org.geotools.swing JMapFrame]))


(def *style-factory*  (CommonFactoryFinder/getStyleFactory nil))

(defn read-sld
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


(defn write-image
  "renders a images"
  [imageout feature-collection & frameoptions]
  (let [image (BufferedImage. 800 600 BufferedImage/TYPE_INT_ARGB)
        graphics (.createGraphics image)
        screen-area (Rectangle. 0 0 800 600)
        map-area (.getBounds feature-collection)]
    (doto (StreamingRenderer.)
      (.setContext (apply make-mapcontext feature-collection frameoptions))
      (.paint graphics screen-area map-area))
    (ImageIO/write image "png" (File. imageout))))
