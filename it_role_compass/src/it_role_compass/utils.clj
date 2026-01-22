(ns it-role-compass.utils)

(defn ordered-values
  "This is needed because Clojure hash-maps do not guarantee iteration order"
  [m key-order]
  (mapv #(get m %) key-order))

(defmacro silently [& body]
  `(binding [*out* (java.io.StringWriter.)]
     ~@body))
