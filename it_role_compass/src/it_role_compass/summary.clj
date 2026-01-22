(ns it-role-compass.summary
  (:require
    [it-role-compass.data-transformations :as dt]))

(defn it-job-position-suitability->string
  [average]
  (cond
    (>= average 8) "You are very likely to enjoy working in IT."
    (>= average 6) "You probably could work in the IT sector."
    (>= average 4) "The IT field might be okay for you, but consider it as one of several options."
    :else          "You probably shouldn't work in the IT sector."))

(defn- it-skills-by-rating-threshold
  [ratings comparison-operator threshold]
  (->> ratings
       (filter (fn [[_ rating]] (comparison-operator rating threshold)))
       (map (fn [[k _]] (name k)))
       sort
       vec))

(defn general-summary
  [user-ratings]
  (let [average     (dt/average (vals user-ratings))
        suitability (it-job-position-suitability->string average)
        strengths   (it-skills-by-rating-threshold user-ratings >= 7)
        weaknesses  (it-skills-by-rating-threshold user-ratings <= 4)]
    {:average     average
     :suitability suitability
     :strengths   strengths
     :weaknesses  weaknesses}))
