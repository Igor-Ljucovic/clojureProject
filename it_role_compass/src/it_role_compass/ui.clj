(ns it-role-compass.ui
  (:require
    [clojure.string :as str]
    [it-role-compass.summary :as summary]))

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

(defn general-summary->string
  [user-ratings]
  (let [{:keys [average suitability strengths weaknesses]} (summary/general-summary user-ratings)]
  (str
    (format "Average it job skill: %.2f\n" (double average))
    suitability "\n"
    "Strong skills: " (str/join ", " strengths) "\n"
    "Weak skills: "   (str/join ", " weaknesses))))

(defn it-job-position-predictions->string
  [lines]
  (str
    "Recommended IT job positions:\n"
    (str/join "\n" lines)))
