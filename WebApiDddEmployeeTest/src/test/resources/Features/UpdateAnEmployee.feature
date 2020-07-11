@wip
Feature: Updating an employee record
  As a user
  I want to PUT record
  So i can do my test



#   you have to create/post and then PUT
  Scenario: Updating a single employee via the services
    Given I have access to base class
    And I have access to the web API or Delete
    When I delete an employee in the web API with "86"
    Then There should be no details