(ns clojure-university-project.core
  (:require 
    [clojure-university-project.utils :as utils]
    [clojure-university-project.data-transformations :as data-transformations]
    [clojure-university-project.expert-system :as expert-system]
    [clojure-university-project.ui :as ui]
    [clojure-university-project.data :as data])
  (:gen-class))

(defn build-summary
  [ratings]
  (let [average     (data-transformations/average (vals ratings))
        suitability (ui/it-job-position-suitability-message average)
        strengths   (utils/it-skills-by-rating-threshold ratings >= 7)
        weaknesses  (utils/it-skills-by-rating-threshold ratings <= 4)]
    {:average     average
     :suitability suitability
     :strengths   strengths
     :weaknesses  weaknesses}))

(defn build-it-job-position-recommendations
  [ratings]
  (-> (expert-system/recommended-it-job-positions ratings)
      (data-transformations/power-kv 4)
      (data-transformations/normalize-to-percent-kv)
      (data-transformations/sort-desc-kv)
      (data-transformations/format-it-job-position-recommendations)))

(defn run-app
  []
  (ui/print-intro!)
  (let [ratings         (ui/ask-all-ratings! data/questions)
        summary         (build-summary ratings)
        recommendations (build-it-job-position-recommendations ratings)]
    (ui/print-summary! summary)
    (ui/print-it-job-position-recommendations! recommendations)))

(defn -main
  [& args]
  (run-app))
