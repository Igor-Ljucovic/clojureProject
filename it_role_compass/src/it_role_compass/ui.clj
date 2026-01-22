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
  (str/join
    "\n"
    ["Tired of all of the slop websites and apps that give you very generic job recommendations such as 'Programmer'?"
     ""
     "Well, IT Role Compass is here to help you find a more specific IT job position that best suits your unique skill set!"
     "(if you are even meant to be working in IT that is)"
     ""
     "Our app offers predictions by an expert in this field, as well as a machine learning model trained on real world data."
     ""
     "However, the data used wasn't meant for this purpose, and some IT job positions may be missing from the recommendations, so it is advised to focus on the results from the expert system instead."
     ""
     "You will be asked to rate your interest and proficiency in various skills from 0-10 (i.e. 7, 5.5, 10, 0)."
     ""
     "Let's get started!"
     ""]))

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
    "All IT job position prediction probabilities from out expert system:\n"
    (str/join "\n" lines)))
