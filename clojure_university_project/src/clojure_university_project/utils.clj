(ns clojure-university-project.utils)

(defn it-skills-by-rating-threshold
  [ratings comparison-operator threshold]
  (->> ratings
       (filter (fn [[_ rating]] (comparison-operator rating threshold)))
       (map (fn [[k _]] (name k)))
       sort
       vec))

(defn ordered-values
  "This is needed because Clojure hash-maps do not guarantee iteration order"
  [m key-order]
  (mapv #(get m %) key-order))

(defmacro silently [& body]
  `(binding [*out* (java.io.StringWriter.)]
     ~@body))
