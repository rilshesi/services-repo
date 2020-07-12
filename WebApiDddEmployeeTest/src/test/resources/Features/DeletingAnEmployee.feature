@wip
Feature: Delete an employee record
  As a Admin user
  I want to DELETE record
  So i can do my test



#   make sure you CREATE/POST before DELETE
  Scenario: Delete an employee via the services (API)
    Given I have access to a base class
    And I have access to the web api URI and headers input was defined
    When I create employee and delete response id
      | FirstName | Surname | Gender | Salary |
      | Steven     | Pound   | Male   | 5000  |
    Then The response should contain no data
