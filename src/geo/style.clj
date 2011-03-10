(ns geo.style
  (:import
   [java.awt Color]
   [org.geotools.styling SLDParser StyleBuilder Rule]
   [org.geotools.factory CommonFactoryFinder]))


(def *style-factory*       (CommonFactoryFinder/getStyleFactory nil))
(def *filter-facotry*      (CommonFactoryFinder/getFilterFactory nil))
(def *style-builder*       (StyleBuilder.))
(def *default-color*       "#000000")
(def *default-opacity*     "1.0")

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

(defn make-point [& options]
  (.createPointSymbolizer *style-builder*))

(defn make-line [& options]
  (.createLineSymbolizer *style-builder*))

(defn make-polygon [hash]
  (let [fill (make-fill (:fill hash))
        stroke (make-stroke (:stroke hash))]
    (.createPolygonSymbolizer *style-factory* stroke fill nil)))

(defn make-rule [symbolizers]
  (let [rule (.createRule *style-builder* (into-array symbolizers))]
    rule))

(defn make-style [rules]
  (let [feature-type-style (.createFeatureTypeStyle *style-factory*
                                                    (into-array Rule rules))
        style (.createStyle *style-factory*)]
    (.add (.featureTypeStyles style)feature-type-style) style))

(def rule-list
  [{:name "Polygon Style"
    :symbolizers
    [{:type "Polygon"
      :symbol {:fill { :color "#1a286d"
                      :opacity ".5"}
               :stroke { :color "#1a286d"
                        :opacity "1.0"
                        :width ".10" } }}]}])

(defn match-symbolizer [type]
  (condp = type
      "Point"   make-point
      "Line"    make-line
      "Polygon" make-polygon))

(defn compile-style [rule-list]
  "Takes a list of rules and returns a style"
  (make-style
   (map (fn [rule]
          (make-rule (map (fn [symbolizer]
                            ((match-symbolizer (:type symbolizer))
                             (:symbol symbolizer) )) (:symbolizers rule))))
        rule-list)))
