(ns clojure-university-project.utils)

(defn it-skills-by-rating-threshold
  [ratings comparison-operator threshold]
  (->> ratings
       (filter (fn [[_ rating]] (comparison-operator rating threshold)))
       (map (fn [[k _]] (name k)))
       sort
       vec))

(defn ordered-map
  "Creates a new map whose iteration order follows `key-order`.
   Missing keys are skipped. Extra keys are ignored."
  [m key-order]
  (into (array-map)
        (map (fn [k] [k (get m k)]))
        key-order))

(defmacro silently [& body]
  `(binding [*out* (java.io.StringWriter.)]
     ~@body))
