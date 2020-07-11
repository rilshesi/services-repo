
@wip
Feature: Delete an employee record
  As a Admin user
  I want to DELETE record
  So i can do my test



#   make sure you CREATE/POST before DELETE
  Scenario: Delete an employee via the services (API)
    Given I have access to a base class
    And I have access to the web api URI and headers input is defined
    When I Get employee with an id "2"
    Then The response should contain data below
      |ID |FirstName |Surname |Gender |Salary |
      |2  |Steve     |Pound   |Male   |45000 |