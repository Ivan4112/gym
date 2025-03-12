@сomponentTest
Feature: Search for Trainings

  Scenario: Trainer searches for their training sessions
    Given a logged-in trainer "test.lastName7"

  Scenario: Trainer searches but has no training sessions
    Given a logged-in trainer "test.lastName6" with no scheduled trainings
