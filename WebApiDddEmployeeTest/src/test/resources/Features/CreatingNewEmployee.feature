@wip
Feature: Creating new employee record
  As a user
  I want to POST new record
  So i can do my test



#   delete after POST/CREATE
  Scenario: Creating a single employee via the services
    Given I have access to base class
    And I have access to the web API
    When I create an employee in the web API
      |FirstName |Surname |Gender |Salary |
      |Thomas     |Steve   |Male   |120000  |
    Then Employee should be created as above and deleted
