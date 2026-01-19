(ns it-role-compass.ui
  (:require
    [clojure.string :as str]))

(defn it-job-position-suitability-message
  [average]
  (cond
    (>= average 8) "You are very likely to enjoy working in IT."
    (>= average 6) "You probably could work in the IT sector."
    (>= average 4) "The IT field might be okay for you, but consider it as one of several options."
    :else          "You probably shouldn't work in the IT sector."))

(defn ask-for-it-skills-ratings
  [question]
  (loop []
    (println question)
    (flush)
    (let [input (read-line)
          value (parse-double input)]
      (if (and value (<= 0 value 10))
        value
        (do
          (println "Please enter a number from 0 to 10 (decimals allowed).")
          (recur))))))

(defn print-intro!
  []
  (println
    "Rate each topic from 0 to 10 (decimals allowed) based on how interested you are in it
            and how good you think you are at it."))

(defn ask-all-ratings!
  [questions]
  (into {}
        (for [{:keys [id q]} questions]
          [id (ask-for-it-skills-ratings q)])))

(defn print-it-job-position-summary!
  [{:keys [average suitability strengths weaknesses]}]
  (println (format "Average it job skill: %.2f" average))
  (println suitability)
  (println "Strong skills:" (str/join ", " strengths))
  (println "Weak skills:"   (str/join ", " weaknesses)))

(defn print-it-job-position-recommendations!
  [lines]
  (println "Recommended IT job positions:")
  (doseq [line lines]
    (println line)))