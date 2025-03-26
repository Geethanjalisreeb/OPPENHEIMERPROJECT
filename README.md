# OPPENHEIMERPROJECT

**OVERVIEW:**
This framework supports web and API testing using various tools and technologies to streamline testing workflows.


**TOOLS AND TECHNOLOGIES:**


**Build Tool:** Maven

**Runner:** TestNG

**Language:** Java

**BDD Framework:** Cucumber

**UI Automation:** Selenium

**API Automation:** Rest Assured

_Note: Framework is developed in Mac OS and hence few changes might be required to run on Windows OS_


**PROJECT STRUCTURE:**


**Feature File Location:** src/test/resources/features

**Sample Report Location:** src/test/resources/output

**API & Web Config Location:** src/test/resources

**Driver File Location:** DriverFiles/chromeDriver<BrowserVersion> (Example: chromeDriver87)


**RUNNING TESTS:**

**Running API Tests:**
You can run the API tests with the following Maven commands. Replace the tag with the appropriate scenario tag:

1. Run tests with tag @InsertWorkClassSingle:
   
mvn clean test -P api -Dcucumber.options="--tags @InsertWorkClassSingle"

2. Run tests with tag @InsertWorkClassMultiple:
   
mvn clean test -P api -Dcucumber.options="--tags @InsertWorkClassMultiple”

3. Run tests with tag @GetTaxRelief:
   
mvn clean test -P api -Dcucumber.options="--tags @GetTaxRelief”

**Run UI Tests:**
You can run the UI tests with the following command. Replace the tag with the appropriate scenario tag:

1. Run tests with tag @UploadFile:
   
mvn clean test -P cucumber -Dcucumber.options="--tags @UploadFile"


**ALLURE REPORTS:**

**Serve Allure Report:**
After tests have been executed, you can serve the Allure report using the following command:

allure serve target/allure-results/

**Generate Report:**
To generate the Allure report and save it to a folder, use the following command:

allure generate -c target/allure-results -o src/test/resources/output/<FolderNametoGenerate>


**Notes:**
Ensure that you have Allure installed and set up for generating and serving reports.

Make sure the required driver files for Selenium are placed correctly under DriverFiles/ based on your browser version (e.g., chromeDriver87 for Chrome version 87).
