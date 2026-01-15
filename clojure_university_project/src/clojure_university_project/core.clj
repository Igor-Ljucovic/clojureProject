(ns clojure-university-project.core
  (:require 
    [clojure-university-project.utils :as utils]
    [clojure-university-project.data-transformations :as data-transformations]
    [clojure-university-project.expert-system :as expert-system]
    [clojure-university-project.ui :as ui]
    [clojure-university-project.data :as data]
    [clojure-university-project.ml.evaluation :as evaluation])
  (:gen-class))

(defn it-job-position-summary
  [ratings]
  (let [average     (data-transformations/average (vals ratings))
        suitability (ui/it-job-position-suitability-message average)
        strengths   (utils/it-skills-by-rating-threshold ratings >= 7)
        weaknesses  (utils/it-skills-by-rating-threshold ratings <= 4)]
    {:average     average
     :suitability suitability
     :strengths   strengths
     :weaknesses  weaknesses}))

(defn it-job-position-recommendations
  [ratings]
  (-> (expert-system/recommended-it-job-position-weights ratings)
      (data-transformations/power-kv 4)
      (data-transformations/normalize-to-percent-kv)
      (data-transformations/sort-desc-kv)
      (data-transformations/format-it-job-position-recommendations)))

(defn run-app
  []
  (ui/print-intro!)
  (let [ratings                    (ui/ask-all-ratings! data/questions)
        summary                    (it-job-position-summary ratings)
        recommendations            (it-job-position-recommendations ratings)
        ml-quant-ratings           (expert-system/expert-system->ml-ratings-data-refactor ratings)
        ml-qual-ratings-unordered  (data-transformations/quant->qual ml-quant-ratings data/ML-LABELS 0 10)
        ml-ratings                 (utils/ordered-values ml-qual-ratings-unordered data/ML-FEATURE-ORDER)]
    (ui/print-summary! summary)
    (ui/print-it-job-position-recommendations! recommendations)
    (evaluation/run-ml! ml-ratings)))

(defn -main
  [& args]
  (run-app))