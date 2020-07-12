@wip
Feature: Updating an employee record
  As a user
  I want to PUT record
  So i can do my test


# PUT (post, put, delete)
  Scenario: Updating a single employee via the services
    Given I have access to base class1
    And I have access to the web API or PUT
    When I post and put an employee in the web API
      | FirstName | Surname | Gender | Salary |
      | Thomas    | Steve   | Male   | 120000 |
    Then Employee should be updated and deleted after