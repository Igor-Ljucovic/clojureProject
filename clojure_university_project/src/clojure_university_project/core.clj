(ns clojure-university-project.core
  (:require 
    [clojure-university-project.utils :as utils]
    [clojure-university-project.data-transformations :as dt]
    [clojure-university-project.expert-system :as es]
    [clojure-university-project.ui :as ui]
    [clojure-university-project.data :as data]
    [clojure-university-project.ml.evaluation :as eval])
  (:gen-class))

(defn it-job-position-summary
  [user-ratings]
  (let [average     (dt/average (vals user-ratings))
        suitability (ui/it-job-position-suitability-message average)
        strengths   (utils/it-skills-by-rating-threshold user-ratings >= 7)
        weaknesses  (utils/it-skills-by-rating-threshold user-ratings <= 4)]
    {:average     average
     :suitability suitability
     :strengths   strengths
     :weaknesses  weaknesses}))

(defn it-job-position-recommendations
  [user-ratings]
  (-> (es/recommended-it-job-position-weights user-ratings)
      (dt/power-kv 4)
      (dt/normalize-to-percent-kv)
      (dt/sort-desc-kv)
      (dt/format-it-job-position-recommendations)))

(defn run-app
  []
  (ui/print-intro!)
  (let [user-ratings               (ui/ask-all-ratings! data/questions)
        es-summary                 (it-job-position-summary user-ratings)
        es-recommendations         (it-job-position-recommendations user-ratings)
        ml-quant-ratings           (es/expert-system->ml-ratings-data-refactor user-ratings)
        ml-qual-ratings-unordered  (dt/quant->qual ml-quant-ratings data/ML-LABELS 0 10)
        ml-ratings                 (utils/ordered-values ml-qual-ratings-unordered data/ML-FEATURE-ORDER)]
    (ui/print-it-job-position-summary! es-summary)
    (ui/print-it-job-position-recommendations! es-recommendations)
    (eval/print-report! (eval/predict! ml-ratings))))

(defn -main
  [& args]
  (run-app))