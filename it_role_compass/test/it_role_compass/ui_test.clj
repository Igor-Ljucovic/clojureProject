(ns it-role-compass.ui-test
  (:require [midje.sweet :refer :all]
            [it-role-compass.ui :as sut]))

(fact "ask-for-it-skills-rating"
  (tabular
    (fact "it returns the value immediately for various valid inputs"
      (let [input-stream (atom [?input])
            mock-read   #(let [val (first @input-stream)] 
                         (swap! input-stream rest) 
                         val)
            mock-write  (fn [_] nil)] ;; We don't need to log output for success tests

        (#'sut/ask-for-it-skills-rating "IT skill question..." mock-read mock-write) => ?expected))
    ?input   ?expected
    "0"      0.0
    "10"     10.0
    "6.75"   6.75
    "8"      8.0)

  (fact "reprompts when input is invalid until a valid one is provided"
    (let [input-stream (atom ["abc" "15" "7.5"])
          output-log   (atom [])
          mock-read   #(let [val (first @input-stream)] 
                       (swap! input-stream rest) 
                       val)
          mock-write  #(swap! output-log conj %)]
      
      (#'sut/ask-for-it-skills-rating "IT skill question..." mock-read mock-write) => 7.5
      (some #{"Please enter a number from 0 to 10 (decimals allowed)."} @output-log) => truthy
      ;; The error message is expected exactly twice (once for "abc", once for "15")
      (count (filter #(= % "Please enter a number from 0 to 10 (decimals allowed).") @output-log)) => 2
      (last @output-log) => "IT skill question..."))

  (fact "passes the correct question to the write-fn"
    (let [written-messages (atom [])
          mock-read        (fn [] "5")
          mock-write      #(swap! written-messages conj %)]
      
      (#'sut/ask-for-it-skills-rating "IT skill question..." mock-read mock-write) => 5.0
      (first @written-messages) => "IT skill question...")))