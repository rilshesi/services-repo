@regression, @get
Feature: Getting employee record
  As a user
  I want to GET record
  So i can do my test



#   GET
  Scenario: Find a single employee via the services (API)
    Given I have access to a base file
    And I have access to the web api URI and headers input is defined
    When I Create and Get employee
      | FirstName | Surname | Gender | Salary |
      | Donald    | Trump   | Male   | 45000  |
    Then The response should contain data below
      | FirstName | Surname | Gender | Salary |
      | Donald    | Trump   | Male   | 45000  |




