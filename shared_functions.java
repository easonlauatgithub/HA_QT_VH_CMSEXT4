package suite;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class shared_functions {
	public static String saveFilePath = "C:\\Users\\LYS678\\Desktop\\img\\";
	public static String EnvironmentValue_TestName = "";
	public static ArrayList<ArrayList<String>> DataTable = new ArrayList<ArrayList<String>>();
	public static int indexOfCurrentlyTestingArrList = -1;
	public static Map<String, String> Parameter = new HashMap<>();
	public static String shell_extjs_version ="";
	public static WebDriver driver;
	
	public static Map<String, String> sam_gor() throws Exception {
		//------------------------------ Sam Gor Code START ------------------------------
		System.out.println("------------------------------ "+EnvironmentValue_TestName+" ------------------------------");
		reporter_ReportEvent( "------------------------------ ", EnvironmentValue_TestName, " ------------------------------" );
		//UFT - regression_test_initialize
		regression_test_initialize(Parameter.get("hospital_code"));
		for(int i=1; i<DataTable.size(); i++) {
			if( DataTable.get(i).get(0).equals( Parameter.get("test") ) ) {
				indexOfCurrentlyTestingArrList = i;
				reporter_ReportEvent( "micDone", "Test name match", DataTable.get(i).get(0)+" = "+ Parameter.get("test") );
			}
		}
		//UFT - regression_test_prepare
		return regression_test_prepare();
		//------------------------------ Sam Gor Code END ------------------------------
	}
	public static void regression_test_initialize(String hospital_code) throws Exception {
		String test_datasheet_basedir = "\\\\dc5wfsns09b\\cs7\\TEAMFOLDER\\CMSIII_RTF_QAG\\HPQTP_testing\\qtp_testdata\\fps_regression_testscript_data";
		String test_category_code = EnvironmentValue_TestName.substring(0, 3);
		String subdir = test_category_code;
		String target_datasheet_filename = test_category_code + "_" + hospital_code + ".xls";
		if( hospital_code.length() != 3 ) {
			shared_functions.reporter_ReportEvent( "micFail", "hospital_code length is NOT 3", "[regression_test_init] expects a 3-character 'hospital_code'; current pass-in value = " + hospital_code );
			//echo("reporter.ReportEvent micFail, \"hospital_code length is NOT 3\", \"[regression_test_init] expects a 3-character 'hospital_code'; current pass-in value = \" & hospital_code");
			//Hardcode EXIT TEST
		}else if( hospital_code!="VH3" ) {
			target_datasheet_filename = test_category_code + "_V" + hospital_code + ".xls";
		}
		//Hardcode CAN'T CREATE FILE FROM SERVER
		String localPath = "C:\\Users\\LYS678\\Desktop\\fps_regression_testscript_data";
		String test_datasheet_filepath =  localPath + "\\" + subdir + "\\" + target_datasheet_filename;
		//String test_datasheet_filepath =  test_datasheet_basedir + "\\" + subdir + "\\" + target_datasheet_filename;
		//Hardcode CAN'T CREATE FILE FROM SERVER
		File fso = new File(test_datasheet_filepath);
		if( !fso.exists() ) {
			shared_functions.reporter_ReportEvent( "micFail", "unable to locate the datasheet", "following datasheet not found: " + test_datasheet_filepath );
			//echo("reporter.ReportEvent micFail, \"unable to locate the datasheet\", \"following datasheet not found: \" & test_datasheet_filepath");
			//Hardcode EXIT TEST
		}
		fso =null;
		DataTable = fnExcelTo2x2ArrayList(test_datasheet_filepath);
		 shared_functions.reporter_ReportEvent( "micDone", "regression_test_init", "datasheet import completed: "+test_datasheet_filepath );
		//echo("reporter.ReportEvent micDone, \"regression_test_init\", \"datasheet import completed: \" "+test_datasheet_filepath);
	}
	public static ArrayList fnExcelTo2x2ArrayList(String filePath) throws Exception {
	   File excelFile = new File(filePath);
	   FileInputStream fis = new FileInputStream(excelFile);
	   HSSFWorkbook workbook = new HSSFWorkbook(fis);
	   HSSFSheet sheet = workbook.getSheetAt(0);
	   int numOfRows = sheet.getPhysicalNumberOfRows();
	   ArrayList<ArrayList<String>> arrListTable = new ArrayList<ArrayList<String>>();
	   for (int i = 0; i < numOfRows ; i++) {
		   HSSFRow row = sheet.getRow(i);
		   if(row==null) {break;}
		   int numOfCols = sheet.getRow(i).getLastCellNum();
		   ArrayList<String> arrListRow = new ArrayList<String>();
		   for (int j = 0; j < numOfCols ; j++) {
			   if(sheet.getRow(i).getCell(j) == null ) {
				   arrListRow.add("");
			   }else {
				   arrListRow.add(sheet.getRow(i).getCell(j).toString());
			   }
		   }
		   arrListTable.add(arrListRow);
	   }
	   workbook.close();
	   fis.close();
	   return arrListTable;
   }
	public static String fnExcelToString(String filePath) throws Exception {
		String strExcelData = "";   
		File excelFile = new File(filePath);
		FileInputStream fis = new FileInputStream(excelFile);
		HSSFWorkbook workbook = new HSSFWorkbook(fis);
		HSSFSheet sheet = workbook.getSheetAt(0);
		int numOfRows = sheet.getPhysicalNumberOfRows();		   
		Iterator<Row> rowIterator = sheet.iterator();
		while(rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			while(cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				strExcelData += cell.toString() + ";";
			}
			strExcelData += "\n";
	   }
		workbook.close();
		fis.close();
		return strExcelData;
	}
	public static Map<String, String> regression_test_prepare() throws Exception {
		 Map<String, String> ret_dict = new HashMap<>();
		 if(indexOfCurrentlyTestingArrList==-1) {
			 shared_functions.reporter_ReportEvent( "micDone", "Test name does not match", shared_functions.Parameter.get("test") );
			 //echo("reporter.ReportEvent micDone, \"Test name does not match\", "+ shared_functions.Parameter.get("test"));
			 return ret_dict;
		 }
		 for(int j=0; j<DataTable.get(indexOfCurrentlyTestingArrList).size(); j++) {
			 String p_name = DataTable.get(0).get(j);
			 String p_value = DataTable.get(indexOfCurrentlyTestingArrList).get(j);
			 //Add header and value into ret_dict
			 ret_dict.put(p_name,p_value);
			 echo("testscript_prepare - ret_dict.Add: " + p_name + " >>> " + p_value);
			 //Add dict_item and value into ret_dict
			 Boolean is_test_parms = p_name.length()>10?p_name.substring(0, 10).equals("test_parms"):false;
			 Boolean is_common_parms = p_name.equals("common_parms");
			 Boolean is_dict_item = p_name.length()>9?p_name.substring(0, 9).equals("dict_item"):false;
			 //vbs Mid(string, start, [length]) --> java string.subString(start, start+length)
			 //vbs InStr([start], strBeSearched, --> java strSearchFor) strBeSearched.indexOf(strSearchFor,[start])
			 if( is_test_parms||is_common_parms||is_dict_item ) {
				 while( p_value.indexOf(":=")>0 ) {
					 int separator_i = p_value.indexOf(":=");
					 String dict_key = p_value.substring(0, separator_i); //mid(p_value, 1, separator_i - 1)
					 String dict_value = "";
					 if( p_value.substring(separator_i+2, separator_i+3).equals("'") ) { //mid(p_value, separator_i + 2, 1) = "'"
						 int next_i1 = p_value.indexOf("'", separator_i+4); //instr(separator_i+4, p_value, "'");
						dict_value = p_value.substring(separator_i+3, separator_i+3+next_i1-(separator_i+3)); //mid(p_value, separator_i + 3, next_i1 - (separator_i + 3))
						p_value = p_value.substring(next_i1 + 3);
					 }else {
						int next_i = p_value.indexOf(";;", separator_i); //instr(separator_i, p_value, ";;")
						if(next_i>0) {
							dict_value = p_value.substring(separator_i+2, separator_i+2+next_i-(separator_i+2)); //dict_value = mid(p_value, separator_i + 2, next_i - (separator_i + 2))		
							//dict_value = convert_string_to_dp_use(dict_value);//dict_value = convert_string_to_dp_use(dict_value)
							p_value = p_value.substring(next_i + 2); //p_value = mid(p_value, next_i + 2)
						}else {
							dict_value = p_value.substring(separator_i + 2);//dict_value =  mid(p_value, separator_i + 2)
							//dict_value = convert_string_to_dp_use(dict_value);//dict_value = convert_string_to_dp_use(dict_value)
							p_value = "";//p_value = ""
						}
					 }
					 echo("testscript_prepare - ret_dict.Add: " + dict_key + " >>> " + dict_value);
					 ret_dict.put(dict_key,dict_value);
				 }
			 }else if( p_name.equals("specialty") || p_name.equals("subspecialty") ) {
				 if(p_value.trim().length()>0 && p_value!=null) {
					ret_dict.put(p_name,p_value);
				 }
			 } 
		 }
		 indexOfCurrentlyTestingArrList = -1;
		//If Browser("title:=HA CMS.*").exist(1) Then
		//		'get the user name from browser title
		//		browser_user = trim(split(Browser("title:=HA CMS.*").GetROProperty("title"),"|")(1))
		//		ret_dict.Add "username", browser_user
		//		ret_dict.Add "username_dp", convert_string_to_dp_use(browser_user)	
		//		Browser("title:=HA CMS.*").Activate
		//End If

		//'get shell extjs version
		//If Browser("title:=HA CMS.*").Page("title:=HA CMS.*").exist(1) Then
		//	shell_extjs_version = "2"
		//	If Browser("title:=HA CMS.*").Page("title:=HA CMS.*").WebElement("html tag:=SPAN","html id:=menuNextPatBtn-btnInnerEl").exist(1) Then
		//		shell_extjs_version = "4"
		//	End If 
		//End If
		//print "shell_extjs_version = " & shell_extjs_version 
		 echo("Hardcode shell_extjs_version = " + shell_extjs_version);
		
		 //On Error Resume Next
		 if( Parameter.get("hospital_code").length()>0 ) {
			 if( Parameter.get("hospital_code") == "VH3" ) {
				 ret_dict.put("hosp_code", "VH");
			 }else {
				 ret_dict.put( "hosp_code", Parameter.get("hospital_code") );
			 }
		 }
		//On Error Goto 0
		//Reporter.Filter = rfEnableAll 
		 shared_functions.reporter_ReportEvent( "micDone", "testscript_prepare", "preparation done, number of items in dictionary: "+ret_dict.size() );
		 //echo("reporter.ReportEvent micDone, \"testscript_prepare\", \"preparation done, number of items in dictionary: \" "+ret_dict.size());
		return ret_dict;
	}
    public static String convert_case_no_to_cms_format(String case_no) {
    	//HN080000642 -> HN08000064(2)
    	int l_case_no = case_no.length();
		String check_case_no = case_no.substring(0, l_case_no-1) + "(" + case_no.substring(l_case_no-1,l_case_no) + ")" ;
		return check_case_no;
	}
	public static String convert_string_to_dp_use(String str) {
	    str = str.replace("\\", "\\\\" );
	    str = str.replace("(", "\\(");
	    str = str.replace(")", "\\)");
	    str = str.replace("[", "\\[");
	    str = str.replace("]", "\\]");
	    str = str.replace(".", "\\.");
	    str = str.replace("*", "\\*");
	    //str = str.replace("-", "\\-");
	    str = str.replace("^", "\\^");
	    str = str.replace("$", "\\$");
	    str = str.replace("|", "\\|");
	    str = str.replace("?", "\\?");
	    str = str.replace("+", "\\+");
		return str;
	}
	public static <E> void echo(E str) {
		System.out.println("echo: " +str);
	}
	public static void hightlightElement(WebDriver driver, String byType, String typeName) throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String elementSelected = null;
		switch(byType) {
			case "id":
				elementSelected = "document.getElementById(\""+typeName+"\")";
				break;
			case "css":
				elementSelected = "document.querySelector(\""+typeName+"\")";
				break;
			default:
				break;
		} 
		String makeBorderRed = ".style.border = \"5px solid red\";";
		String makeBorderNone = ".style.border = \"none\";";
		String scriptMakeBorderRed = elementSelected + makeBorderRed;
		String scriptMakeBorderNone = elementSelected + makeBorderNone;
		js.executeScript(scriptMakeBorderRed);
		sleepForAWhile(100);
		js.executeScript(scriptMakeBorderNone);
		sleepForAWhile(100);
		js.executeScript(scriptMakeBorderRed);
		sleepForAWhile(100);
		js.executeScript(scriptMakeBorderNone);
		sleepForAWhile(100);
		js.executeScript(scriptMakeBorderRed);		
	}
	public static void showWindowID(WebDriver driver) {
		System.out.print("SHOW ALL WINDOW ID: ");
		Set<String> handles = driver.getWindowHandles();
		for (String h : handles) {
			System.out.print(h);
			System.out.print(",");
			//driver.switchTo().window(h);
		}
		System.out.println("");
	}
	public static void switchWindowID(WebDriver driver) {
		System.out.print("SWITCH ALL WINDOW ID: ");
		Set<String> handles = driver.getWindowHandles();
		for (String h : handles) {
			System.out.print(h);
			System.out.print(",");
			driver.switchTo().window(h);
		}
		System.out.println("");
	}
	public static void showAlert(WebDriver driver) throws InterruptedException {
		if( isAlertPresent(driver) ) {
			Alert a = driver.switchTo().alert();
			System.out.println("ALERT");
			System.out.println(a);
		}else {
			System.out.println("NO ALERT");
		}		
	}
	public static void handleAlert(WebDriver driver, Boolean bConfirm) throws InterruptedException {
		if( isAlertPresent(driver) ) {
			Alert a = driver.switchTo().alert();
			System.out.print("ALERT: ");
			System.out.println(a);
			if(bConfirm) {a.accept();}else {a.dismiss();}
		}else {
			System.out.println("NO ALERT");
		}		
	}
	/*
	public static void handleSPIOAlert1(WebDriver driver, Boolean bConfirm) throws InterruptedException {
		//UnhandledAlertException: Modal dialog present with text:
		//Unable to call common API to refresh SPIO, 
		//Unable to get property 'refreshSPIO' of undefined or null reference
		if( isAlertPresent(driver) ) {
			Alert a = driver.switchTo().alert();
			System.out.print("SPIOAlert: ");
			System.out.println(a);
			if(bConfirm) {a.accept();}else {a.dismiss();}
			//sleep 30s
		}else {
			System.out.println("No SPIOAlert");
		}		
	}
	*/
	public static void showIframe(WebDriver driver) {
		List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
		System.out.println("iframes size: "+iframes.size());
		int counter =0;
		for (WebElement iframe : iframes) {
			counter++;
			System.out.println("------ iframes("+counter+") -------");
			//System.out.println("iframes: "+iframe);
			System.out.println("name: "+iframe.getAttribute("name"));
			System.out.println("id: "+iframe.getAttribute("id"));
			//System.out.println("cls: "+iframe.getAttribute("class"));
	    }		
	}
    public static boolean isAlertPresent(WebDriver d) throws InterruptedException
    {
		try {
			System.out.print("isAlertPresent, ");
			sleepForAWhile(3000);
			d.switchTo().alert(); 
			return true;
		}catch (TimeoutException e) { //wait for XXX s but no element
			return false;
		}catch (NoAlertPresentException e) { //switch to alert failed
			return false;
		}
        //try{d.switchTo().alert(); return true;}catch (NoAlertPresentException e1){return false;}
    }
	public static void loginToProxyServerRobot(WebDriver d, String login, String pwd) {
		try {
			Alert alert = d.switchTo().alert();
			Robot robot = new Robot();
	        if( alert != null ) {
				alert.sendKeys(login);
				robot.keyPress(KeyEvent.VK_TAB);
				StringSelection stringSelection = new StringSelection(pwd);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V); 
				robot.keyRelease(KeyEvent.VK_V); 
				robot.keyRelease(KeyEvent.VK_CONTROL); 
		        alert.accept();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void loginToProxyServer(WebDriver d, String login, String pwd) {
		try {
			Alert alert = d.switchTo().alert();
	        if( alert != null ) {
	        	alert.sendKeys(login + Keys.TAB.toString() + pwd);
		        alert.accept();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static class getCurrentClass extends SecurityManager {
		public String fnGetClassName() {
			return getClassContext()[1].getName();
		}
	}
	public static String getClassName() {
		return new getCurrentClass().fnGetClassName();
	}
	public static String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	public static String getDateIn(String dFormat) {
		Date date = new Date();
		Locale.setDefault(Locale.US);//MMM 11ды/Nov
		DateFormat dateFormat = new SimpleDateFormat(dFormat);
		return dateFormat.format(date);
	}
	public static String getDateInddMMyyyySlash() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateFormat.format(date);
	}
	public static String getDateInMMMyyyy() {
		Date date = new Date();
		Locale.setDefault(Locale.US);
		DateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
		return dateFormat.format(date);
	}
	public static String getDateInddMMMyyyy() {
		Date date = new Date();
		Locale.setDefault(Locale.US);
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		return dateFormat.format(date);
	}
	public static String getDateInddMMyyyy() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		return dateFormat.format(date);
	}
	public static String getDateIndMyyyy() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy");
		return dateFormat.format(date);
	}
	public static Date addExtraMinute(Date curDate, int minutes) {
		Calendar c = Calendar.getInstance();
		c.setTime(curDate);
		c.add(Calendar.MINUTE, 60);
		Date newDate = c.getTime();
		return newDate;
	}
	public static String getHHMM(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String h = "00"+Integer.toString(c.get(Calendar.HOUR_OF_DAY));
		String m = "00"+Integer.toString(c.get(Calendar.MINUTE));
		String str = h.substring(h.length()-2) + m.substring(m.length()-2);
		return str;
	}
	public static void capScreen(WebDriver d) throws Exception {
		capScreen(d, getClassName());
	}
	public static void capScreen(WebDriver d, String saveAsFileName) throws Exception {
		String strDateTime = getTime();
		String fileName = saveAsFileName+"_"+strDateTime+".jpg";
		//System.out.println("shared_function.capScreen..., save as "+saveFilePath+fileName);
		TakesScreenshot screenShotDriver = (TakesScreenshot) d;
		File src = screenShotDriver.getScreenshotAs(OutputType.FILE);
		File dest =  new File(saveFilePath+fileName);
		try {
			FileUtils.copyFile(src, dest);	
		}catch(IOException e) {
			e.printStackTrace();
		}		
	}
	public static void genReport(String str1, String optionalFileName) {
		String strDateTime = getTime();
		String fileName = optionalFileName+"_"+strDateTime+".txt";
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(saveFilePath+fileName, true)));
		    out.print(strDateTime+": ");
		    out.println(str1+"\n");
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void genReport(String str1) {
		String fileName = getClassName();
		genReport(str1, fileName);
	}
	public static void genPageSourceReport(String strFileName) {
		genReport(driver.getPageSource(), strFileName);
	}
	public static void reporter_ReportEvent(String EventStatus, String ReportStepName, String Details ) {
		String strDateTime = getTime();
		String fileName = "report.txt";
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(saveFilePath+fileName, true)));
		    out.print(strDateTime);
		    out.print(", "+EventStatus);
		    out.print(", "+ReportStepName);
		    out.print(", "+Details);
		    out.println(" ");
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void do_screen_capture_with_filename(WebDriver d, String saveAsFileName) throws Exception {
		System.out.print("do_screen_capture_with_filename..., save as "+saveAsFileName+", ");
		sleepForAWhile(1000); // wait for loading
		String fileName = saveAsFileName+".jpg";
		TakesScreenshot screenShotDriver = (TakesScreenshot) d;
		File src = screenShotDriver.getScreenshotAs(OutputType.FILE);
		File dest =  new File(saveFilePath+fileName);
		try {
			FileUtils.copyFile(src, dest);	
		}catch(IOException e) {
			e.printStackTrace();
		}		
	}

	public static void clickAndType(WebElement e, String str){
		try{
			e.clear();
			e.click();
			e.sendKeys(str);
			e.sendKeys(Keys.ENTER);			
		}catch(Exception ex){
			ex.printStackTrace();
			clickAndType(e, str);
		}
	}
	public static void clearAndSend(WebElement e, String str){
		e.click();
		e.clear();
		e.click();
		e.sendKeys(Keys.HOME);
		e.sendKeys(str);
		e.sendKeys(Keys.ENTER);
	}	
	public static void countTestPassed(int steps_passed, int total_steps) {
		if (steps_passed == total_steps) {
			shared_functions.reporter_ReportEvent("micDone", "test passed", total_steps+" step(s) passed");
		}else {
			shared_functions.reporter_ReportEvent("micFail", "test failed", "steps passed/total steps: "+steps_passed+"/"+total_steps);
		}		
	}
	public static Boolean isExistElement(String cssSelector) {
		return driver.findElements(By.cssSelector(cssSelector)).size()>0;
	}
	public static Boolean isCorrectText(String cssSelector, String strForCheck) {
		if( isExistElement(cssSelector) ) {
			Boolean isCorrect = driver.findElement(By.cssSelector(cssSelector)).getText().equals(strForCheck);
			if(isCorrect) {
				return true;
			}
		}
		return false;
	}
	public static void printListSize(String css) {
		System.out.println(driver.findElements(By.cssSelector( css )).size());
	}
	public static void printListText(String css) {
		List<WebElement> list = driver.findElements(By.cssSelector( css ));
		System.out.println("list"+list.size());
		for(int i=0; i<list.size(); i++) {
			System.out.println("list("+i+"): "+list.get(i).getText());
		}		
	}
	public static void clickNonDisplayedElement(WebDriver d, WebElement e) {
		JavascriptExecutor jse = (JavascriptExecutor)d;
		jse.executeScript("arguments[0].click();", e);		
	}	
	public static String encodePwd(String strToEncode) throws UnsupportedEncodingException {
        String base64encodedString = Base64.getEncoder().encodeToString(strToEncode.getBytes("utf-8"));
        return base64encodedString;
	}
	public static String decodePwd(String strEncoded) throws UnsupportedEncodingException {
        byte[] base64decodedBytes = Base64.getDecoder().decode(strEncoded);
        String base64decodedString = new String(base64decodedBytes, "utf-8");
		return base64decodedString;
	}
	public static String getTableText(WebElement eTable, int rowNum, int colNum){
		System.out.print("--getTableText:");
		String str = null;
		List<WebElement> li1 = eTable.findElements(By.tagName("tr"));
		WebElement e1 = li1.get(rowNum);
		List<WebElement> li2 = e1.findElements(By.tagName("td"));
		WebElement e2 = li2.get(colNum);
		str = e2.getText();
		System.out.print(str);
		System.out.println("--");
		return str;
	}
	public static void showTableText(WebElement eTable){
		List<WebElement> liRows = eTable.findElements(By.tagName("tr"));
		System.out.println("numOfRows:"+liRows.size());
		for(int i=0; i<liRows.size(); i++){
			WebElement eRow = liRows.get(i);
			List<WebElement> liCols = eRow.findElements(By.tagName("td"));
			System.out.println("Row("+i+") numOfCols:"+liCols.size());
			for(int j=0; j<liCols.size(); j++){
				WebElement eCol = liCols.get(j);
				System.out.println("Row("+i+"),Col("+j+"):"+eCol.getText());
			}
		}
	}
	public static void exit_test_iteration() {
		System.exit(1);
		//driver.quit();
	}
	public static void right_click(WebElement elm, int x, int y){
		Actions action= new Actions(driver);
		action.moveToElement(elm, x, y).contextClick().build().perform();
	}
	public static void clickBtnByCssWithException(String strCss){
		Boolean bBtnIsClicked = false;
		while(!bBtnIsClicked){
			try {
				System.out.println("clickBtnByCssWithException:"+strCss);
				List<WebElement> liBtn = driver.findElements(By.cssSelector(strCss));
				if(liBtn.size()>0){
					WebElement eBtn = liBtn.get(0);
					eBtn.click();
					bBtnIsClicked = true;
				}
			}catch(Exception e) {
				e.printStackTrace();
				bBtnIsClicked = false;
			}
		}
	}
	public static void sleepForAWhile(int s) throws InterruptedException{
		System.out.println("Thread.sleep:"+s);
		Thread.sleep(s);
	}
	

}
