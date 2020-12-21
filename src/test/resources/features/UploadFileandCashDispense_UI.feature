@UploadFile
Feature: Upload File and Dispense Cash

  Background: Cleanup and Login
  Given I am Navigating to Oppenheimer Home Page

  Scenario: Upload File as a Clerk
    When I upload file as a clerk
    Then I should see the below values are displayed in the table as Bookkeeper
    |NatId|TaxRefliefAmount|
    |VOI9$$$$$|40271.00    |
    |VOI8$$$$$|40271.00    |
    And Verify total amount and working class hero count are displayed as "80542.00" and "2" respectively

  Scenario: Verify Dispense Now button
    Then verify text and color of the dispense button is "Dispense Now" and "rgba(220, 53, 69, 1)" respectively
    When I click on Dispense Now button as Governor
    Then I should see "Cash dispensed" message is displayed



