(ns geo.render
  (:import [org.geotools.data FeatureSource]
           [org.geotools.map DefaultMapContext MapContext]
           [javax.swing JFrame]
           [org.geotools.swing JMapFrame]))

(defn swing [feature-collection]
  (doto (JMapFrame.
   (doto (DefaultMapContext.)
     (.setTitle "Swing!!!!!!!!!!!")
     (.addLayer feature-collection nil)))
    (.setDefaultCloseOperation (JFrame/DISPOSE_ON_CLOSE))
    (.setSize 800 600)
    (.enableStatusBar true)
    (.enableToolBar true)
    (.setVisible true)))
