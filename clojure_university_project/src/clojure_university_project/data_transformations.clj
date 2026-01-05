(ns clojure-university-project.data-transformations
  (:require
    [clojure.string :as str]))

(defn average 
  [numbers]
  (/ (apply + numbers) (double (count numbers))))

(defn normalize-to-percent-kv
  [jobs]
  (let [total (apply + (map second jobs))]
    (map (fn [[label rating]]
           [(* 100 (/ rating total)) label]) 
         jobs)))

(defn sort-desc-kv
  [col]
  (sort-by first > col))

(defn format-it-job-position-recommendations
  [normalized-data]
  (map (fn [[rating label]] (format "%.1f%% %s" rating label)) 
       normalized-data))

(defn power-kv
  [kvs power]
  (map (fn [[k v]] [k (Math/pow (double v) power)]) kvs))

(defn quant->qual 
  [m labels min-val max-val]
  (let [n    (count labels)
        step (/ (- max-val (double min-val)) n)]
    (update-vals m #(nth labels (min (dec n) (int (max 0 (/ (- % min-val) step))))))))
