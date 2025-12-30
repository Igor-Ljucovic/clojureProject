(ns clojure-university-project.core
  (:require [criterium.core :as c]
            [clojure.string :as str])
  (:gen-class))

(def questions
  ["How interesting do you find working with large amounts of data and trying to find useful conclusions in it? (0-10)"
   "How good are you at advanced math (integrals, derivatives, functions with multiple variables)? (0-10)"
   "How interesting do you find data structures and solving algorithmic problems? (0-10)"
   "How good are you at mechanical and electrical physics? (0-10)"
   "How interesting does working with 2D and 3D geometry in programming sound to you (0-10)?"
   "How fun does abstracting to more general rules sound to you? (0-10)"
   "How interesting does optimizing memory and making things run as fast and efficiently as possible sound to you? (0-10)"
   "How interesting do you find designing/engineering new things and how they work? (0-10)"
   "How interesting do you find working with hardware? (0-10)"
   "How fun does making things look nice and be simple and intuitive sound? (0-10)"
   "How good are you at identifying weak spots in things or people? (0-10)"
   "How good are you at finding the only wrong way to do something? (0-10)"
   "How interesting does testing, thinking about edge cases and predicting bugs in code sound? (0-10)"
   "How interesting do you find working with people, understanding their problems and solving them? (0-10)"
   "How much do you care about how technology feels in everyday life and how people interact with it moment-to-moment? (0-10)"
   "How exciting is the idea of building something interactive that reacts differently based on the user's actions? (0-10)"
   "How fun is it for you to imagine how something could go wrong, and how you'd prevent it? (0-10)"
   "How much does it interest you to make things that can grow from small to huge without breaking? (0-10)"
   "How interesting does statistics and probability sound to you? (0-10)"
   "How naturally does it come to you to predict what someone will find confusing or difficult before they even try it? (0-10)"
   "How satisfying is it for you to take messy information and structure it in a clean, efficient way? (0-10)"
   "How patient are you when someone doesn't understand something, and you need to explain it calmly? (0-10)"
   "How willing are you to spend a long time figuring out why something doesn't work, even if the reason is tiny or obscure? (0-10)"
   "Would you mind solving the same problems the majority of the time at work? (0-10)"
   "How much do you care about how much money you'll make? (0-10)"])

;; had to separate parsing to avoid "Cannot recur across try" error
(defn- parse-rating-double
  [s]
  (try
    (Double/parseDouble s)
    (catch NumberFormatException _ nil)))

(defn ask-for-it-topics-ratings
  [question]
  (loop []
    (println question)
    (flush)
    (let [input (read-line)
          value (parse-rating-double input)]
      (if (and value (<= 0 value 10))
        value
        (do
          (println "Please enter a number from 0 to 10 (decimals allowed).")
          (recur))))))

;; HACK: code duplication
(defn build-profile
  [[data math algorithms physics geometry abstraction optimization engineering hardware ui edge-cases testing
    people ux statistics empathy simplification patience debugging monotony money]]
  {:data              data
   :math              math
   :algorithms        algorithms
   :physics           physics
   :geometry          geometry
   :abstraction       abstraction
   :optimization      optimization
   :engineering       engineering
   :hardware          hardware
   :ui                ui
   :edge-cases        edge-cases
   :testing           testing
   :people            people
   :ux                ux
   :statistics        statistics
   :empathy           empathy
   :simplification    simplification
   :patience          patience
   :debugging         debugging
   :monotony          monotony
   :money             money})

;; HACK: code duplication
(defn strong-areas
  [profile threshold]
  (let [filtered (filter (fn [[_ score]] (>= score threshold)) profile)
        names    (map (fn [[k _]] (name k)) filtered)]
    (vec (sort names))))

;; HACK: code duplication
(defn weak-areas
  [profile threshold]
  (let [filtered (filter (fn [[_ score]] (<= score threshold)) profile)
        names    (map (fn [[k _]] (name k)) filtered)]
    (vec (sort names))))

;; HACK: code duplication
(defn recommended-it-job-positions
  [m]
  (let [data           (:data m)
        math           (:math m)
        algorithms     (:algorithms m)
        physics        (:physics m)
        geometry       (:geometry m)
        abstraction    (:abstraction m)
        optimization   (:optimization m)
        engineering    (:engineering m)
        hardware       (:hardware m)
        ui             (:ui m)
        edge-cases     (:edge-cases m)
        testing        (:testing m)
        people         (:people m)
        ux             (:ux m)
        statistics     (:statistics m)
        empathy        (:empathy m)
        simplification (:simplification m)
        patience       (:patience m)
        debugging      (:debugging m)
        monotony       (:monotony m)
        money          (:money m)

        results
        (-> []
            (conj (str (+ (* data 16) (* statistics 14) (* math 12))
                       "|data analyst, data science, ML, AI"))
            (conj (str (+ (* engineering 16) (* algorithms 14) (* optimization 12))
                       "|backend development, systems engineering"))
            (conj (str (+ (* engineering 14) (* debugging 16) (* monotony 12))
                       "|DevOps"))
            (conj (str (+ (* hardware 16) (* engineering 12) (* physics 12))
                       "|embedded systems, IoT, firmware, robotics, hardware-related"))
            (conj (str (+ (* geometry 14) (* algorithms 14) (* optimization 14) (* math 12))
                       "|game development, simulations, graphics"))
            (conj (str (+ (* ui 16) (* simplification 14) (* people 10))
                       "|frontend development, mobile development"))
            (conj (str (+ (* ux 16) (* empathy 16) (* people 10))
                       "|UX/UI design, product design"))
            (conj (str (+ (* testing 16) (* edge-cases 16) (* debugging 14) (* monotony 10))
                       "|QA engineering, test automation"))
            (conj (str (+ (* edge-cases 16) (* debugging 16))
                       "|security")))]
    results))


(defn it-job-suitability-messages [average]
  (cond
    (>= average 8) "You are very likely to enjoy working in IT."
    (>= average 6) "You probably should work in the IT sector."
    (>= average 4) "The IT field might be okay for you, but consider it as one of several options."
    :else          "You probably shouldn't work in the IT sector."))

; used for testing purposes, as a stub for "ask-for-it-topics-ratings" questions for ratings
(defn random-ratings
  [n min-val max-val]
  (vec
    (repeatedly n
      #(let [r (+ min-val (* (rand) (- max-val min-val)))] 
       (/ (Math/round (* 100 r)) 100.0)))))

;; HACK: single responsibility principle violation
(defn run-app 
  []
  (println "Rate each topic from 0 (hate it) to 10 (love it).")
  (let [ratings (mapv ask-for-it-topics-ratings questions)
        profile (build-profile ratings)
        total (reduce + ratings)
        average (/ (double total) (count ratings))
        it-job-suitability (it-job-suitability-messages average)
        strengths (strong-areas profile 7)
        weaknesses (weak-areas profile 4)
        recommended-it-job-positions (recommended-it-job-positions profile)]
    (println (format "Average interest: %.2f" average))
    (println it-job-suitability)
    (println "Strong areas:" (str/join ", " strengths))
    (println "Weak areas:" (str/join ", " weaknesses))
    (println "Recommended IT job positions:" (str/join ", " recommended-it-job-positions))))

(defn -main
  [& args]
  (run-app))
