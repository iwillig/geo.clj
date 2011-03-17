(ns geo.style
  (:import
   [java.awt Color]
   [org.geotools.styling SLD]
   [org.geotools.styling SLDParser StyleBuilder Rule]
   [org.geotools.factory CommonFactoryFinder]))

(def *style-factory*       (CommonFactoryFinder/getStyleFactory nil))
(def *filter-facotry*      (CommonFactoryFinder/getFilterFactory nil))
(def *style-builder*       (StyleBuilder.))

(defn read-sld
  [path]
  (first
   (.readXML (SLDParser. *style-factory* (-> path java.io.File. .toURL)))))

(defn hex->color [hex]
  (Color/decode hex))

(defn make-literal [literal]
  (.literal *filter-facotry* literal))

(defn make-fill [{:keys [color opacity]}]
  (.createFill *style-factory*
                 (make-literal (hex->color color))
                 (make-literal opacity)))

(defn make-stroke [{:keys [color width opacity]}]
  (.createStroke *style-factory*
                 (make-literal (hex->color color))
                 (make-literal width)
                 (make-literal opacity)))

;; simple styling with the SLD interface.

(defn make-point [{:keys [well-know-name line-color fill-color opacity size label font]
                  :or {well-know-name "Square"
                       line-color "#808080"
                       fill-color "#ffffff"
                       opacity 1.0
                       size 1.0 }}]
  (SLD/createPointStyle well-know-name (hex->color line-color) (hex->color fill-color)
                        opacity
                        size
                        label
                        font))

(defn make-line [{:keys [line-color width label font]
                  :or { line-color "#808080" width 1.0}}]
  (SLD/createLineStyle (hex->color line-color) width label font))

(defn make-polygon [{:keys [storke fill opacity label font]
                :or   {storke "#808080" fill "#ffffff" opacity 1.0}}]
  (SLD/createPolygonStyle (hex->color storke) (hex->color fill) opacity label font))




;;; more complex styling
;;; not finished 
(defn make-rule [symbolizers]
  (let [rule (.createRule *style-builder* (into-array symbolizers))]
    rule))

(defn make-style [rules]
  (let [feature-type-style (.createFeatureTypeStyle *style-factory*
                                                    (into-array Rule rules))
        style (.createStyle *style-factory*)]
    (.add (.featureTypeStyles style)feature-type-style) style))

