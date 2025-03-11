@IntegrationTest
Feature: Gym and TrainerWorkloadService integration

  Scenario: Adding a training sends a message to TrainerWorkloadService
    Given the GymService is running
    And the TrainerWorkloadService is running
    When a training is added for trainer "test.lastName7" with duration 4 minutes
    Then TrainerWorkloadService should receive the training event

  Scenario: Deleting a training sends a message to TrainerWorkloadService
    Given a training exists for trainer "test.lastName7" with ID 61
    When the training with ID 61 is deleted
    Then TrainerWorkloadService should receive the training deletion event

  Scenario: Fetching trainer's monthly workload
    Given the trainer "test.lastName7" has completed trainings
    When the workload summary is requested for "test.lastName7"
    Then the response should contain workload statistics
