@ComponentTest
Feature: Training Management

  Scenario: Successfully add a training session
    Given logg-in trainer "test.lastName7"
    When the trainer adds a training session with details:
      | date       | duration | client |
      | 2025-06-15 | 3       | John   |
    Then the system should confirm the training session was added

  Scenario: Failed to add training due to missing client
    Given logg-in trainer "test.lastName7"
    When the trainer attempts to add a training session with missing client
    Then the system should return an error message "Client cannot be null"

  Scenario: Successfully delete a training session
    Given logg-in trainer "test.lastName7"
    And an existing training session with ID "10"
    When the trainer deletes the training session
    Then the system should confirm the training session was deleted

  Scenario: Failed to delete a non-existent training session
    Given logg-in trainer "test.lastName7"
    When the trainer attempts to delete a training session with ID "9999"
    Then the system should return an error "Training session not found"
