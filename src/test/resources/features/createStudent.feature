Feature: Create student

#  @wip @api
  Scenario: Create student a teacher and verify status code 201
    Given I logged Bookit api as "teacher"
    When I send POST request to "/api/students/student" endpoint with following information
      | first-name      | Din               |
      | last-name       | Djarin                |
      | email           | dindjarin@gmail.com |
      | password        | abc123               |
      | role            | student-team-leader  |
      | campus-location | VA                   |
      | batch-number    | 7                    |
      | team-name       | BugBusters           |
    Then status code should be 201
    And I delete previously added student

