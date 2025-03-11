@ComponentTest
Feature: Search for Trainings

  Scenario: Trainer searches for their training sessions
    Given a logged-in trainer "test.lastName7"
#    When the trainer searches for their training sessions
#    Then the system should return a list of their scheduled trainings

  Scenario: Trainer searches but has no training sessions
    Given a logged-in trainer "test.lastName6" with no scheduled trainings
#    When the trainer searches for their training sessions
#    Then the system should return an empty list
