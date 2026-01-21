(ns it-role-compass.core
  (:require 
    [it-role-compass.utils :as utils]
    [it-role-compass.data-transformations :as dt]
    [it-role-compass.expert-system :as es]
    [it-role-compass.ui :as ui]
    [it-role-compass.data :as data]
    [it-role-compass.ml.model :as model]
    [it-role-compass.ml.inference :as inference]
    [it-role-compass.ml.ui :as ml-ui])
  (:gen-class))

(defn- it-job-position-summary
  [user-ratings]
  (let [average     (dt/average (vals user-ratings))
        suitability (ui/it-job-position-suitability->string average)
        strengths   (utils/it-skills-by-rating-threshold user-ratings >= 7)
        weaknesses  (utils/it-skills-by-rating-threshold user-ratings <= 4)]
    {:average     average
     :suitability suitability
     :strengths   strengths
     :weaknesses  weaknesses}))

(defn- it-job-position-recommendations
  [user-ratings]
  (-> (es/recommended-it-job-position-weights user-ratings)
      (dt/power-kv 4)
      (dt/normalize-to-percent-kv)
      (dt/sort-desc-kv)
      (dt/format-it-job-position-recommendations)))

(defn- run-app
  []
  (println (ui/app-intro->string))
  (let [user-ratings               (ui/ask-all-ratings! data/questions)
        es-summary                 (it-job-position-summary user-ratings)
        es-recommendations         (it-job-position-recommendations user-ratings)
        ml-quant-ratings           (es/expert-system->ml-ratings-data-refactor user-ratings)
        ml-qual-ratings-unordered  (dt/quant->qual ml-quant-ratings data/ML-LABELS 0 10)
        ml-ratings                 (utils/ordered-values ml-qual-ratings-unordered data/ML-FEATURE-ORDER)]
    (println (ui/it-job-position-summary->string es-summary))
    (println (ui/it-job-position-recommendations->string es-recommendations))
    (println (ml-ui/machine-learning-report->string (inference/predict! ml-ratings)))))

(defn- -main
  [& args]
  (run-app))