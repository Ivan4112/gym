@IntegrationTest
Feature: Authorization

  Scenario: Successful login
    Given a registered user with username "test.lastName7" and password "UJeZDn75Kx"
    When the user attempts to log in with valid credentials
    Then the system should return a valid authentication token
