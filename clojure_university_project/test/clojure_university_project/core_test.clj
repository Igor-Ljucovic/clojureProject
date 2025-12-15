(ns clojure-university-project.core-test
  (:require [midje.sweet :refer :all]
            [clojure-university-project.core :as sut]))

(facts
  (+ 1 2) => 3)

(facts
  (+ 1 2) => 7)

(facts
  (+ 1 "3") => 7)
