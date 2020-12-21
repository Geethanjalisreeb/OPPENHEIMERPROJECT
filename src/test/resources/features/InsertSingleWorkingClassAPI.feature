@InsertWorkClassSingle
Feature: Insert Single Work Class

  Background: Cleanup Database before execution
    Given Database is cleaned up before test execution


  Scenario Outline: <TestScenario_Success>
    When I send post request with path "/calculator/insert" and request as in "InsertWorkingClass" with following values
      |Birthday  |Gender  |Name  |Natid  |Salary  |Tax  |
      |<Birthday>|<Gender>|<Name>|<Natid>|<Salary>|<Tax>|
    Then I should validate status code is "202"

    Examples:
      |TestScenario_Success                                                          |Birthday    |Gender       |Name                |Natid        |Salary     |Tax                   |
      |Insert Work Class with gender M                                               |19081988    |F            |T11Name              |T111111      |345678     |250                  |
      |Insert Work Class with gender F                                               |19081988    |M            |T12Name              |T122222      |345678     |250                  |
      |Insert Work Class with decimal Salary                                         |19081988    |F            |T13Name              |T133333      |345678.5556|250                  |
      |Insert Work Class with decimal Tax                                            |19081988    |F            |T14Name              |T144444      |345678     |250.4555             |
      |Insert Work Class with spl char in Name                                       |19081988    |F            |@@$$%^O&O            |T155555      |345678.5556|250                  |
      |Insert Work Class with spl char in Natid                                      |19081988    |F            |T15Name              |@@$$%^O&O    |345678     |250.4555             |

  Scenario Outline: <TestScenario_Failure>
    When I send post request with path "/calculator/insert" and request as in "InsertWorkingClass" with following values
      |Birthday  |Gender  |Name  |Natid  |Salary  |Tax  |
      |<Birthday>|<Gender>|<Name>|<Natid>|<Salary>|<Tax>|
    Then I should validate status code is "500"

    Examples:
      |TestScenario_Failure                                                        |Birthday    |Gender       |Name                |Natid        |Salary     |Tax                   |
      |Insert Work Class with invalid gender                                       |19081988    |E            |T21Name              |T211111      |345678     |250                  |
      |Insert Work Class with null gender                                          |19081988    |             |T22Name              |T222222      |345678     |250                  |
      |Insert Work Class with null Salary                                          |19081988    |F            |T23Name              |T233333      |           |250                  |
      |Insert Work Class with invalid Salary                                       |19081988    |F            |T24Name              |T244444      |rtt        |250.4555             |
      |Insert Work Class with null Tax                                             |19081988    |F            |@@$$%^O&O            |T255555      |345678.5556|                     |
      |Insert Work Class with invalid Tax                                          |19081988    |F            |T25Name              |@@$$%^O&O    |345678     |fgg                  |
      |Insert Work Class with invalid Birthday                                     |56081988    |F            |T26Name              |T266666      |345678     |250.4555             |
      |Insert Work Class with null Birthday                                        |            |F            |T27Name              |T277777      |345678     |250.4555             |
      |Insert Work Class with Tax value more than Salary                           |20081996    |F            |T28Name              |T288888      |3456       |233422             |