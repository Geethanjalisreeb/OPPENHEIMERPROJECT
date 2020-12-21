@InsertWorkClassMultiple
Feature: Insert Multiple Work Class

  Background: Cleanup Database before execution
    Given Database is cleaned up before test execution


  Scenario: Insert multiple work class list
    When I send post request with path "/calculator/insertMultiple" and request as in "InsertMultipleWorkingClass" with following values
      |Birthday1  |Gender1  |Name1  |Natid1  |Salary1  |Tax1  |Birthday2  |Gender2  |Name2  |Natid2  |Salary2  |Tax2  |
      |13081998   |F        |T31Name |T311111 |345678   |250   |14081968   |M        |T32Name |T322222 |3678     |250   |
    Then I should validate status code is "202"


  Scenario Outline: <TestScenario_Failure>
    When I send post request with path "/calculator/insertMultiple" and request as in "InsertMultipleWorkingClass" with following values
      |Birthday1  |Gender1  |Name1  |Natid1  |Salary1  |Tax1  |Birthday2  |Gender2  |Name2  |Natid2  |Salary2  |Tax2  |
      |<Birthday1>|<Gender1>|<Name1>|<Natid1>|<Salary1>|<Tax1>|<Birthday2>|<Gender2>|<Name2>|<Natid2>|<Salary2>|<Tax2>|
    Then I should validate status code is "500"

    Examples:
      |TestScenario_Failure                                                        |Birthday1  |Gender1  |Name1  |Natid1  |Salary1  |Tax1  |Birthday2  |Gender2  |Name2  |Natid2  |Salary2  |Tax2  |
      |Insert Work Class with 1 invalid workclass in list                          |13081998   |Female        |T41Name |T411111 |345678   |250   |14081968   |M        |T5Name |T422222 |3678     |250   |
      |Insert Work Class with both invalid workclass in list                       |13081998   |Female        |T43Name |T433333 |345678   |250   |14081968   |Male     |T2Name |T444444 |3678     |250   |
