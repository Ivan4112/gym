@сomponentTest
Feature: Trainer Statistics

  Scenario: Get monthly workload summary
    Given logging trainer "test.lastName7"
    When the trainer requests their monthly workload summary
    Then the system should return a workload summary report

  Scenario: Request workload summary for a non-existent trainer
    Given a logged-in user
    When the trainer requests the monthly workload summary for "unknown_trainer"
    Then the system should return an error "Trainer not found"
