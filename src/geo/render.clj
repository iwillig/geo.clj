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
  (first (.readXML (SLDParser. *style-factory* (-> path java.io.File. .toURL)))))

(defn make-map
  "builds a DefaultMapContent"
  [feature-collection sld]
  (doto (DefaultMapContext.)
    (.setTitle "Default Map")
    (.addLayer feature-collection (make-style sld))))

(defn swing [feature-collection sld]
  (doto (JMapFrame.
   (doto (DefaultMapContext.)
     (.setTitle "Swing!!!!!!!!!!!")
     (.addLayer feature-collection (make-style sld))))
    (.setDefaultCloseOperation (JFrame/DISPOSE_ON_CLOSE))
    (.setSize 800 600)
    (.enableStatusBar true)
    (.enableToolBar true)
    (.setVisible true)))

(defn render-image
  "renders a images"
  [feature-collection sld]
  (make-map feature-collection sld))
