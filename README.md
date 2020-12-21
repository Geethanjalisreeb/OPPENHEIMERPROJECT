# OPPENHEIMERPROJECT
Overview:
This framework supports web and api testing and details are as follows:
Build Tool - Maven
Runner - TestNG
Language - Java
BDD Framework - Cucumber
UI Automation - Selenium
API Automation - Rest Assured

Note: Framework is developed in Mac OS and hence few changes might be required to run on Windows OS

Test Scenarios:
Feature File Location: src/test/resources/features
Sample Report Location: src/test/resources/output
API & Web Config Location: src/test/resources
DriverFileLocation: DriverFiles/chromeDriver<BrowserVersion> (Eg: chromeDriver87)

Run API Tests:
mvn clean test -P api -Dcucumber.options="--tags @InsertWorkClassSingle"
mvn clean test -P api -Dcucumber.options="--tags @InsertWorkClassMultiple”
mvn clean test -P api -Dcucumber.options="--tags @GetTaxRelief”

Run UI Tests:
mvn clean test -P cucumber -Dcucumber.options="--tags @UploadFile"

Serve Allure Report:
allure serve target/allure-results/

Generate Report:
allure generate -c target/allure-results -o src/test/resources/output/<FolderNametoGenerate>

