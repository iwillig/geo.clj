(ns geo.render
  (:use geo.io)
  (:import [org.geotools.data FeatureSource]
           [javax.imageio ImageIO]
           [java.io ByteArrayOutputStream]
           [java.io ByteArrayInputStream]
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



(defn viewer
    "create a swing frame displaying the features in the geotools
   featurecollection. See make-mapcontext for options"
    [geo-collection  & frameoptions]
    (let [ gt-collection ( make-eager-feature-collection geo-collection)
          mapcontext (make-mapcontext)]
      (.addLayer mapcontext gt-collection nil)
      (doto (JMapFrame. mapcontext)
       (.setDefaultCloseOperation (JFrame/DISPOSE_ON_CLOSE))
       (.setSize 800 600)
       (.enableStatusBar true)
       (.enableToolBar true)
       (.setVisible true))))


(defn render-image
  "renders a images"
  [imageout feature-collection & mapoptions]
  (let [image (BufferedImage. 800 600 BufferedImage/TYPE_INT_ARGB)
        graphics (.createGraphics image)
        extent (.getBounds feature-collection)
        screen-area (Rectangle. 0 0 800 600)
        mapcontext (apply make-mapcontext mapoptions) ]
    (.addLayer mapcontext feature-collection nil)
    (doto (StreamingRenderer.)
      (.setJava2DHints
       (RenderingHints. RenderingHints/KEY_ANTIALIASING RenderingHints/VALUE_ANTIALIAS_ON))
      (.setContext  mapcontext)
      (.paint graphics screen-area extent))
    (ImageIO/write image "png" (File. imageout))))


(defn render-stream
  [feature-collection]
  (let [image (BufferedImage. 600 400 BufferedImage/TYPE_INT_ARGB)
          graphics (.createGraphics image)
          output (ByteArrayOutputStream.)
          screen-area (Rectangle. 0 0 600 400)
          extent (.getBounds feature-collection)
          mapcontext (make-mapcontext)]
    (.addLayer mapcontext feature-collection nil)
    (doto (StreamingRenderer.)
      (.setJava2DHints
       (RenderingHints. RenderingHints/KEY_ANTIALIASING RenderingHints/VALUE_ANTIALIAS_ON))
      (.setContext mapcontext)
      (.paint graphics screen-area extent))
    (ImageIO/write image "png" output)
   (ByteArrayInputStream. (.toByteArray output))))


