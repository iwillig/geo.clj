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
      (.setTransparent false))))



(defn viewer
    "create a swing frame displaying the features in the geotools
   featurecollection. See make-mapcontext for options"
    [gt-collection & frameoptions]
    (let [mapcontext (apply make-mapcontext frameoptions)]
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
  (let [image-format    (or (:image-format mapoptions) "png")
        height          (or (:height mapoptions) 600)
        width           (or (:width  mapoptions) 600) 
        image           (BufferedImage. width height BufferedImage/TYPE_INT_ARGB)
        graphics        (.createGraphics image)
        extent          (.getBounds feature-collection)
        screen-area     (Rectangle. 0 0 width height)
        mapcontext      (apply make-mapcontext mapoptions)]
    (.addLayer mapcontext feature-collection nil)
    (doto (StreamingRenderer.)
      (.setJava2DHints
       (RenderingHints.
        RenderingHints/KEY_ANTIALIASING RenderingHints/VALUE_ANTIALIAS_ON))
      (.setContext  mapcontext)
      (.paint graphics screen-area extent))
    (ImageIO/write image "png" (File. imageout))))


(defn render->stream
  [feature-collection extent
   & {:keys [height width]
      :or [height 100 width 100]}]
  (let [
        image (BufferedImage. width height BufferedImage/TYPE_INT_ARGB)
        graphics (.createGraphics image)
        output (ByteArrayOutputStream.)
        screen-area (Rectangle. 0 0 width height)
        mapcontext (make-mapcontext)]
    (.addLayer mapcontext feature-collection nil)
    (doto (StreamingRenderer.)
      (.setJava2DHints
       (RenderingHints.
        RenderingHints/KEY_ANTIALIASING RenderingHints/VALUE_ANTIALIAS_ON))
      (.setContext mapcontext)
      (.paint graphics screen-area extent))
    (ImageIO/write image "png" output)
    (ByteArrayInputStream. (.toByteArray output))))


