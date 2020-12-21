package CucumberWeb.Utilities;

import CucumberWeb.RunnerClasses.CukesRunnerTest;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DataHelper {

    private static Logger log = Logger.getLogger(DataHelper.class.getName());
    public void setTestID(String testID) {
        TestID = testID;
    }
    public String getTestID() {
        return TestID;
    }
    private static String TestID;
    public static Map<String, List<String>> data()
    {


        Map<String, List<String>> testdatamap = new HashMap<String, List<String>>();
        try
        {

            XSSFSheet Execsheet = ExcelUtils.GetExcelSheet(CukesRunnerTest.TestDataPath, CukesRunnerTest.DataSheetName);
            //Row HeaderRow = Execsheet.getRow(0);

            for(int i=1;i<Execsheet.getPhysicalNumberOfRows();i++)
            {
                //String StePFlagVal = ExcelUtils.getCellData(i, 3);
                //if ("Y".equals(StePFlagVal)){
                XSSFRow currentRow = Execsheet.getRow(i);
                List<String> currentList = new ArrayList<String>();
                String TestStepID = ExcelUtils.getCellData(i, 1);
                for(int j=3;j<currentRow.getPhysicalNumberOfCells();j++)
                {
                    String TestStepVal = ExcelUtils.getCellData(i, j);
                    currentList.add(TestStepVal);
                }
                if (!currentList.isEmpty()){
                    testdatamap.put(TestStepID,currentList);
                }
                //}
            }


        }
        catch (Exception e)
        {
            log.error(e.toString());
        }

        return testdatamap;

    }


    public static String Getdata(String ParamID )
    {

        ArrayList<String> TestDataList = new ArrayList<String>();
        String ParamName = ParamID.split("_")[0];
        String testdata = null;
        try
        {
            for (int i=1;i<200;i++) {
                if (CukesRunnerTest.hm.containsKey(TestID + "_" + i)){
                    TestDataList = (ArrayList<String>) CukesRunnerTest.hm.get(TestID + "_" + i);
                    for (String str : TestDataList) {
                        if (str.contains(ParamName)){
                            testdata = str.split(": ",2)[1];
                            break;
                        }
                    }
                }
            }


        }
        catch (Exception e)
        {
            log.error(e.toString());
        }

        return testdata;

    }


    public static ArrayList<String> GetExecutionList() throws Exception{

        XSSFSheet Execsheet = ExcelUtils.GetExcelSheet(CukesRunnerTest.TestDataPath, CukesRunnerTest.ExecSheetName);

        ArrayList<String> ExecutionList = new ArrayList<String>();

        try {

            int lastRowIndex = Execsheet.getLastRowNum();
            //System.out.println(lastRowIndex);

            //Add Executable Scenarios to the Execution List
            for (int i=2;i<=lastRowIndex+1;i++) {
                String ExecFlagVal = ExcelUtils.getCellData(i, 2);
                if ("Y".equals(ExecFlagVal)) {
                    ExecutionList.add(ExcelUtils.getCellData(i, 0));
                }
            }


        } catch (Exception e) {

            log.error(e.toString());
        }
        return ExecutionList;
    }
}