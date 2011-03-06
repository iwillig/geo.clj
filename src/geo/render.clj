(ns geo.render
  (:use geo.io)
  (:import [org.geotools.data FeatureSource Query
            DefaultFeatureResults]
           [javax.imageio ImageIO]
           [java.io
            File
            ByteArrayOutputStream
            ByteArrayInputStream
            FileOutputStream]
           [org.geotools.map DefaultMapContext
            MapContext GraphicEnhancedMapContext]
           [javax.swing JFrame]
           [java.awt Color RenderingHints]
           [org.geotools.renderer.lite StreamingRenderer]
           [java.awt Rectangle]
           [java.awt.image BufferedImage] 
           [org.geotools.swing JMapFrame]))

(defn make-mapcontext
  "builds a DefaultMapContext
    Options can be:
      :title \"Title of JFrame\""
  [& options]
  (let [[title bgcolor transparent] options]
    (doto (GraphicEnhancedMapContext.)
      (.setTitle (or title "Default Map"))
      (.setBgColor (or bgcolor Color/white))
      (.setTransparent (or transparent false)))))

(defn make-map [map-config]
  (let [mapcontext (apply make-mapcontext
                          (vals (dissoc map-config :layers)))]
    (doseq [layer (:layers map-config)]
       (.addLayer mapcontext (first layer) (second layer)))
    mapcontext))

(defn make-jmapframe [map-context]
  (doto (JMapFrame. map-context)
    (.setDefaultCloseOperation (JFrame/DISPOSE_ON_CLOSE))
       (.setSize 800 600)
       (.enableStatusBar true)
       (.enableToolBar true)
       (.setVisible true)))

(defmulti viewer class)

(defmethod viewer
  GraphicEnhancedMapContext
  [map-context]
  (make-jmapframe map-context))

(defmethod viewer
  DefaultFeatureResults
    [gt-collection & frameoptions]
    (let [map-context (apply make-mapcontext frameoptions)]
      (.addLayer map-context gt-collection nil)
      (make-jmapframe map-context)))

(defn make-render [map-context graphics screen-area extent]
    (doto (StreamingRenderer.)
      (.setJava2DHints
       (RenderingHints.
        RenderingHints/KEY_ANTIALIASING RenderingHints/VALUE_ANTIALIAS_ON))
      (.setContext  map-context)
      (.paint graphics screen-area extent)))

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
    (make-render mapcontext graphics screen-area extent)
    (ImageIO/write image "png" (File. imageout))))


(defn render-stream
  [feature-collection extent
   & {:keys [height width style]
      :or {height 100 width 100 style nil}}]
  (let [image (BufferedImage. width height BufferedImage/TYPE_INT_ARGB)
        graphics (.createGraphics image)
        output (ByteArrayOutputStream.)
        screen-area (Rectangle. 0 0 width height)
        mapcontext (make-mapcontext)]
    (.addLayer mapcontext feature-collection style)
    (make-render mapcontext graphics screen-area extent)
    (ImageIO/write image "png" output)
    (ByteArrayInputStream. (.toByteArray output))))


