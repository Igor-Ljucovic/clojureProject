(ns clojure-university-project.core
  (:require 
    [clojure-university-project.utils :as utils]
    [clojure-university-project.data-transformations :as data-transformations]
    [clojure-university-project.expert-system :as expert-system]
    [clojure-university-project.ui :as ui]
    [clojure-university-project.data :as data]
    
    [clojure.string :as str])
  (:gen-class))

;; HACK: single responsibility principle violation
(defn run-app 
  []
  (println "Rate each topic from 0 (hate it) to 10 (love it).")
   (let [
        ratings (into {} (for [{:keys [id q]} data/questions]
                          [id (ui/ask-for-it-topics-ratings q)]))
        average (data-transformations/average (vals ratings))
        it-job-suitability (ui/it-job-suitability-messages average)
        strengths (utils/it-skills-by-rating-threshold ratings >= 7)
        weaknesses (utils/it-skills-by-rating-threshold ratings <= 4)
        job-positions (expert-system/recommended-it-job-positions ratings)]

    (println (format "Average interest: %.2f" average))
    (println it-job-suitability)
    (println "Strong skills:" (str/join ", " strengths))
    (println "Weak skills:" (str/join ", " weaknesses))
    (println "Recommended IT job positions:")
    (doseq [line (-> job-positions
                 (data-transformations/power-transform-job-ratings 4 data/rating-label-separator-regex)
                 (data-transformations/normalize-job-ratings-to-percent)
                 (data-transformations/rank-job-ratings-desc)
                 (data-transformations/format-jobs))]
    (println line))))

(defn -main
  [& args]
  (run-app))
