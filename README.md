# IT Role Compass

A CLI application that helps users determine which IT job positions best fit them based on their interests, skills, and personal preferences using an expert system and a machine learning algorithm.

---

## Features & Architecture

- Predicts suitable IT career paths based on user-provided questionnaire responses
- Supports both expert system-based and machine learning-based prediction models
- Outputs ranked predictions with percentage-based confidence scores
- Automatic translation of user input between expert system and machine learning data formats
- Automated testing for nearly all non-trivial functions using Midje
- Performance testing using Criterium

---

## Use Case

Many career recommendation tools only suggest broad industries such as "IT" or "Healthcare".
IT Role Compass helps users narrow that down further by recommending specific IT job roles, assisting users who are unsure which specialization within the IT industry suits them best.

---

## Tech Stack

- **Programming Language:** Clojure
- **Frameworks & Libraries:** Midje, Criterium, Scicloj, Metamorph, Smile

---

## Additional Notes

The machine learning model was trained on a transformed real-world IT employee dataset that was cleaned, merged, and refined to improve prediction quality.  
The expert system weights were manually designed to simulate expert decision-making logic.

This project focuses heavily on software architecture, prediction logic, code quality, and maintainability rather than UI/visual design.
