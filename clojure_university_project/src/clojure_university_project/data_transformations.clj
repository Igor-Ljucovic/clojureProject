(ns clojure-university-project.data-transformations
  (:require
    [clojure.string :as str]))

(defn average 
  [numbers]
  (/ (apply + numbers) (double (count numbers))))

(defn normalize-job-ratings-to-percent
  [pairs]
  (let [total (apply + (map first pairs))]
    (map (fn [[rating label]] [(* 100 (/ rating total)) label]) pairs)))

(defn rank-job-ratings-desc
  [rating-label-pairs]
  (sort-by first > rating-label-pairs))

(defn format-jobs 
  [normalized-data]
  (map (fn [[rating label]] (format "%.1f%% %s" rating label)) 
       normalized-data))

(defn power-transform-job-ratings 
  "Power is used to adjust the distribution of ratings so that the users can better know
  which jobs suit them best. Instead of linear scaling, exponential scaling is used.
  power=4 is recommended, since it will differentiate better between close ratings. 
  Instead of having almost all the percentages in the 15-25% range, it will spread them out more."
  [jobs power rating-label-separator-regex]
  (map #(let [[rating label] (str/split % rating-label-separator-regex)] 
          [(Math/pow (Double/parseDouble rating) power) label]) jobs))

(defn quant->qual 
  [m labels min-val max-val]
  (let [n    (count labels)
        step (/ (- max-val (double min-val)) n)]
    (update-vals m #(nth labels (min (dec n) (int (max 0 (/ (- % min-val) step))))))))
