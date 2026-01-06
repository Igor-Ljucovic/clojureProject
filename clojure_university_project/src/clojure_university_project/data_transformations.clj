(ns clojure-university-project.data-transformations
  (:require
    [clojure.string :as str]))

(defn average 
  [numbers]
  (/ (apply + numbers) (double (count numbers))))

(defn normalize-to-percent-kv
  [jobs]
  (let [total (apply + (vals jobs))]
    (update-vals jobs #(double (* 100 (/ % total))))))

(defn sort-desc-kv
  [col]
  (sort-by second > col))

(defn format-it-job-position-recommendations
  [normalized-data]
  (map (fn [[label rating]] 
         (format "%.1f%% %s" (double rating) label)) 
       normalized-data))

(defn power-kv
  [kvs power]
  (update-vals kvs #(Math/pow (double %) power)))

(defn quant->qual 
  [m labels min-val max-val]
  (let [n    (count labels)
        step (/ (- max-val (double min-val)) n)]
    (update-vals m #(nth labels (min (dec n) (int (max 0 (/ (- % min-val) step))))))))
