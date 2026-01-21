(ns it-role-compass.ui
  (:require
    [clojure.string :as str]))

(defn it-job-position-suitability->string
  [average]
  (cond
    (>= average 8) "You are very likely to enjoy working in IT."
    (>= average 6) "You probably could work in the IT sector."
    (>= average 4) "The IT field might be okay for you, but consider it as one of several options."
    :else          "You probably shouldn't work in the IT sector."))

(defn- parse-rating
  [s]
  (let [v (parse-double s)]
    (when (and v (<= 0.0 v 10.0))
      (double v))))

(defn- ask-for-it-skills-rating
  [question read-fn write-fn]
  (loop []
    (write-fn question)
    (flush)
    (let [input (read-fn)
          value (parse-rating input)]
      (if value
        value
        (do
          (write-fn "Please enter a number from 0 to 10 (decimals allowed).")
          (recur))))))

(defn ask-for-it-skills-ratings
  [question]
  (ask-for-it-skills-rating question read-line println))

(defn app-intro->string
  []
  (str
    "Rate each topic from 0 to 10 (decimals allowed) based on how interested you are in it
            and how good you think you are at it."))

(defn- ask-all-ratings
  [ask-fn questions]
  (into {}
        (map (fn [{:keys [id q]}]
               [id (ask-fn q)]))
        questions))

(defn ask-all-ratings!
  [questions]
  (ask-all-ratings ask-for-it-skills-ratings questions))

(defn it-job-position-summary->string
  [{:keys [average suitability strengths weaknesses]}]
  (str
    (format "Average it job skill: %.2f\n" (double average))
    suitability "\n"
    "Strong skills: " (str/join ", " strengths) "\n"
    "Weak skills: "   (str/join ", " weaknesses)))

(defn it-job-position-recommendations->string
  [lines]
  (str
    "Recommended IT job positions:\n"
    (str/join "\n" lines)))