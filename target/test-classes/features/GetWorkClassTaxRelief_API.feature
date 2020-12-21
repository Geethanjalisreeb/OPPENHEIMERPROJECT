@GetTaxRelief
Feature: GetTaxReliefValues

  Background: Cleanup Database before execution
    Given I send post request with path "/calculator/rakeDatabase"

  Scenario Outline: <TestScenario_Success>
    When I send post request with path "/calculator/insert" and request as in "InsertWorkingClass" with following values
      |Birthday  |Gender  |Name  |Natid  |Salary  |Tax  |
      |<Birthday>|<Gender>|<Name>|<Natid>|<Salary>|<Tax>|
    Then I should validate status code is "202"
    When I send get request with path "/calculator/taxRelief"
    Then I should verify the value matches response in "GetTaxRelief" with following values
      |NatidMasked  |TaxRelief  |
      |<NatidMasked>|<TaxRelief>|
    When I send get request with path "/calculator/taxReliefSummary"
    Then I should verify the value matches response in "GetTaxReliefSummary" with following values
      |TotalWorkingClassHeroes  |TotalTaxReliefAmount  |
      |1                        |<TaxRelief>|
   And I should verify JSONSchema matches schema as in "GetTaxReliefSummary"
    Examples:
      |TestScenario_Success                                                               |Birthday    |Gender       |Name                |Natid        |Salary     |Tax                   |NatidMasked|TaxRelief|
      |Insert Work Class with Age less than 18 (Masked Value & Gender F)                  |19082004    |F            |T51Name              |T511111      |345678     |250                  |T511$$$    |345928.00|
      |Insert Work Class with Age equal 18 (Masked Value & Gender M)                      |19082002    |M            |T52Name              |T522222      |345678     |250                  |T522$$$    |345428.00|
      |Insert Work Class with Age greater than 18 and less than 35 (Normal Rounding Rule) |19082000    |F            |T53Name              |T533333      |345678.5   |250                  |T533$$$    |276842.80|
      |Insert Work Class with Age equal 35 (Normal Rounding Rule)                         |19081985    |M            |T54Name              |T544444      |345678     |250.35               |T544$$$    |276342.12|
      |Insert Work Class with Age greater than 35 and less than 50 (Tax Relief is 0.00)   |19081980    |M            |T55Name              |T555555      |345678     |345678               |T555$$$    |50.00    |
      |Insert Work Class with Age equal 50 (Tax Relief is 50.00)                          |19081970    |M            |T56Name              |T566666      |345678     |345578               |T566$$$    |50.00    |
      |Insert Work Class with Age greater than 50 and less than 75 (0.00< TaxRelief<50.00)|19081965    |M            |T57Name              |T577777      |345678     |345544.6667          |T577$$$    |50.00    |
      |Insert Work Class with Age equal 75  (0.00< TaxRelief<50.00 & Gender F)            |19081945    |F            |T58Name              |T588888      |345678     |345600               |T588$$$    |528.63   |
      |Insert Work Class with Age equal 76  (Decimal Truncate to 2)                       |19081944    |F            |T59Name              |T599999      |345678.345 |250.567              |T599$$$    |171771.39|
      |Insert Work Class with Age greater than 76                                         |19081940    |M            |T50Name              |T500000      |345678     |250                  |T500$$$    |171271.40|
