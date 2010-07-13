(ns geo.render
  (:import [org.geotools.data FeatureSource]
           [javax.imageio ImageIO]
           [java.io FileOutputStream]
           [org.geotools.map DefaultMapContext MapContext GraphicEnhancedMapContext]
           [org.geotools.data Query]
           [javax.swing JFrame]
           [java.io File]
           [java.awt Color RenderingHints]
           [org.geotools.renderer.lite StreamingRenderer]
           [java.awt Rectangle]
           [java.awt.image BufferedImage] 
           [org.geotools.swing JMapFrame]))



(defn make-mapcontext
  "builds a DefaultMapContext
    Options can be:
      :title \"Title of JFrame\""
  [& mapoptions]
  (let [options (apply hash-map mapoptions)]
    (doto (GraphicEnhancedMapContext.)
      (.setTitle (or (:title options) "Default Map"))
      (.setBgColor Color/white)
      (.setTransparent false)
     (.setTransparent false))))



(defn swing
    "create a swing frame displaying the features in the geotools
   featurecollection. See make-mapcontext for options"
    [feature-collection  & frameoptions]
    (let [mapcontext (make-mapcontext)]
      (.addLayer mapcontext feature-collection nil)
      (doto (JMapFrame. mapcontext)
       (.setDefaultCloseOperation (JFrame/DISPOSE_ON_CLOSE))
       (.setSize 800 600)
       (.enableStatusBar true)
       (.enableToolBar true)
       (.setVisible true))))


(defn write-image
  "renders a images"
  [imageout extent feature-collection & mapoptions]
  (let [image (BufferedImage. 800 600 BufferedImage/TYPE_INT_ARGB)
        graphics (.createGraphics image)
        screen-area (Rectangle. 0 0 800 600)
        mapcontext (apply make-mapcontext mapoptions) ]
    (doseq [feature feature-collection] (.addLayer mapcontext  (:feature  feature) (:style feature)))
    (doto (StreamingRenderer.)
      (.setJava2DHints (RenderingHints. RenderingHints/KEY_ANTIALIASING RenderingHints/VALUE_ANTIALIAS_ON))       
      (.setContext  mapcontext)
      (.paint graphics screen-area extent))
    (ImageIO/write image "png" (File. imageout))))


