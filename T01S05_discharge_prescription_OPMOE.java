package suite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class T01S05_discharge_prescription_OPMOE {
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_DischargePrescription dp = null;
	int steps_passed ;
	int total_steps ; 
	Map<String, String> dict = new HashMap<>();
	String moe_case_no = null;
	String specialty = null;
	String subspecialty = null;
	String version = null;
	String check_today_date = null;
	String check_case_no = null; 
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T01S05_discharge_prescription_OPMOE";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T01S05_extjs4");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		moe_case_no = dict.get("case_no");
		specialty = dict.get("specialty");
		subspecialty = dict.get("subspecialty");
		version = "web";
		check_today_date = shared_functions.getDateIn("dd/MM/yyyy");
		check_case_no = shared_functions.convert_string_to_dp_use(moe_case_no);
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		dp = new PAGE_PatientDetailPage_DischargePrescription(driver);
		steps_passed = 0;
		total_steps = 9;
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		open_moe_function();
		test_check_history();
		test_new_moe_order();
		test_update_moe_order();
		test_remove_order();
		open_moe_function();
		test_drugset();
		open_moe_function();
		test_standard_regimen();
		cmsMainPage.fnNextPatient();
		shared_functions.countTestPassed(steps_passed, total_steps);
	}
	public void open_moe_function() throws Exception {
		System.out.println("open_moe_function - START");
		cmsMainPage.fnMoe();
		cmsMainPage.selectPatientByCaseNum(moe_case_no);
		cmsMainPage.selectSpecialty(specialty, subspecialty);
		psf.closeExistingAlertReminderWindow();
		if(dp.isExistPrescription()){
			dp.clickOkBtn();
			dp.clickDeleteOrderBtn();
			dp.confirmDelete();
		}
		if(dp.isExistSavePMSDataFailed()){dp.clickOKBtnForSavePMSDataFailed();}
		if(dp.isExistFailToRetrievePMSInformation()){
			shared_functions.do_screen_capture_with_filename(driver, "T01S05 - PMS unavailabitlity");
			shared_functions.reporter_ReportEvent("micFail", "QAG_failure", "Unable to retrieve PMS information");
			cmsMainPage.fnNextPatient();
		}
		if(dp.isExistFutureAppointmentDate()){dp.closeFutureAppointmentDate();}
		if(dp.isExistPreviousPrescription()){dp.clickCancelBtn();}
		System.out.println("open_moe_function - END");
	}
	public void close_opmoe_without_save() throws InterruptedException {
		System.out.println("close_opmoe_without_save - START");
		cmsMainPage.fnNextPatient();
		if(dp.isExistLeavingPrescriptionPopup()){
			dp.clickNoForSavePrescritionFirst();
		}
		if( shared_functions.isAlertPresent(driver) ) {
			shared_functions.handleAlert(driver, false);
		}
		System.out.println("close_opmoe_without_save - END");
	}
	public void test_check_history() throws Exception {
		System.out.println("test_check_history - START");
		dp.clickHistoryBtn();
		String hisotry_record_table_innertext = dp.getContentOfPreviousPrescriptionTable();
		int number_of_items = Integer.parseInt(dict.get("check_history_items"));
		int history_items_found = 0;
		for(int i=0; i<number_of_items; i++){
			String suffix = "_"+(i+1);
			String check_history_text = dict.get("check_history_innertext"+suffix);
			if(hisotry_record_table_innertext.indexOf(check_history_text)>0){
				history_items_found = history_items_found + 1;
			}
		}
	    shared_functions.do_screen_capture_with_filename(driver, "T01S05_1");
	    if(history_items_found == number_of_items){
	    	steps_passed = steps_passed + 1;
	    	shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "check history - expected history items found");
	    	System.out.println("test_check_history - verify QAG_checkpoint_1 check history - expected history items found");
	    }else{
	    	shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_1", "check history - only " + history_items_found + " items found");
	    }
	    dp.inputStartDateDuration(dict.get("drugset_duration"));
	    dp.clickCancelBtn();
		System.out.println("test_check_history - END");
	}
	public void test_new_moe_order() throws Exception {
		System.out.println("test_new_moe_order - START");
		int number_of_drugs = Integer.parseInt(dict.get("new_moe_drugs_num"));
		for(int i=0; i<number_of_drugs; i++){
			String suffix = "_d"+(i+1); //_d1 _d2
			dp.inputDrugKeyword(dict.get("new_moe_keyword"+suffix));
			if(dp.checkIfExistImg()){
				dp.selectDrugName(dict.get("new_moe_drugname"+suffix));
			}
			dp.selectDrugType(dict.get("new_moe_drugtype"+suffix));
			dp.inputCommonDosage(dict.get("new_moe_common_dosage"+suffix));
			dp.clickAddBtn();
		    //If moe_frame.WebTable("html tag:=TABLE","cols:=11","column names:=Dosage;;Dosage Unit;Daily Frequency;Suppl\. Frequency;PRN;Dispense;Route/Site;;Duration;Quantity","visible:=True","visible:=True").exist(1) Then
				//'moe_frame.WebEdit("html tag:=INPUT","x:=731","y:=110","visible:=True").Set dict.item("new_moe_duration")
				//moe_frame.WebTable("html tag:=TABLE","column names:=;;¡¿","visible:=True").WebEdit("html tag:=INPUT","visible:=True","class:=moe-ui-textfield.*","visible:=True").Set dict.item("new_moe_duration")
		    	//Wait 1
		    	//moe_frame.WebElement("html tag:=DIV","innerhtml:=<U>A</U>dd","visible:=True","index:=0").Click
		    	//Wait 2
		    //End If
		}
		int number_of_record_found = 0;
		for(int i=0; i<number_of_drugs; i++){
			String suffix = "_d"+(i+1);
			String str = dict.get("new_moe_check"+suffix);
			Boolean isExistMoe = dp.getMoeByDrugNameNDosage(str).size()>0;
			if(isExistMoe){
				number_of_record_found = number_of_record_found + 1;	
			}
		}
		shared_functions.do_screen_capture_with_filename(driver, "T01S05_2");
		if(number_of_drugs == number_of_record_found){
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "new moe order - all drug added is/are found on the screen");
			System.out.println("test_new_moe_order - verify QAG_checkpoint_2 new moe order - all drug added is/are found on the screen");
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_2", "new moe order - only "+number_of_record_found+" is/are found on the screen");
		}
		dp.clickSaveBtn();
		//'save advance Rx
		//If moe_frame.WebElement("class:=x-window-header-text","innertext:=Question / Action \(1-703-1-Q-207\)","visible:=True").Exist(1) Then
			//moe_frame.WebTable("html tag:=TABLE","column names:=;2\. As Advance Rx;","visible:=True").Click
			//Wait 2
		//End If
		//'sometimes there is printer driver error
		//If moe_frame.WebEdit("html tag:=TEXTAREA","innertext:=No prescription will be printed because of system error\.","visible:=True").exist(1) Then
			//reporter.ReportEvent micWarning, "unable to print the prescription", ""
			//moe_frame.WebTable("html tag:=TABLE","column names:=;OK;","visible:=True").Click
			//Wait 2
		//End If
		//UnhandledAlertException: Modal dialog present with text: 
		//Error in timeout-callback: Object doesn't support property or method 'jsPrint': Error in timeout-callback: Object doesn't support property or method 'jsPrint'
		shared_functions.handleAlert(driver, true);
		dp.clickHistoryBtn();
		shared_functions.do_screen_capture_with_filename(driver, "T01S05_3");
		String hisotry_record_table_innertext = dp.getContentOfPreviousPrescriptionTable();
		String check_record_text =  check_today_date + " " + shared_functions.convert_case_no_to_cms_format(moe_case_no);
		if(dp.checkIfExistNewRecord(hisotry_record_table_innertext,check_record_text)){
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "new moe order - the newly saved moe prescription is found in history screen");
			System.out.println("test_new_moe_order - verify QAG_checkpoint_3 new moe order - the newly saved moe prescription is found in history screen");
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_3", "new moe order - the newly saved moe prescription is NOT found in history screen");
		}
		dp.clickCancelBtn();
		System.out.println("test_new_moe_order - END");
	}
	public void test_update_moe_order() throws Exception {
		System.out.println("test_update_moe_order - START");
		String suffix = "_d1"; 
		String str = dict.get("new_moe_check" + suffix); //PIRITON (CHLORPHENIRAMINE MALEATE) tablet<BR>oral : 4 mg tds for 1 weeks
		List<WebElement> liMoes = dp.getMoeByDrugNameNDosage(str);		
		if(liMoes.size()>0){
			liMoes.get(0).click();
		}
		dp.clickEditBtn();
		dp.inputDuration(dict.get("update_moe_duration"));
		dp.inputSpecialInstruction(dict.get("update_moe_instruction"));
		dp.clickAcceptBtn();
		str = dict.get("new_moe_keyword_d1"); //PIRITON
		List<WebElement> liMoes2 = dp.getMoeByDrugKeyword(str);
		if(liMoes2.size()>0){
			WebElement eUpdatedMoe = liMoes2.get(0);
			String strUpdatedMoe = eUpdatedMoe.getText();
			String strInstruction = dict.get("update_moe_instruction");
			String strDuration = dict.get("update_moe_duration")+ " weeks";
			Boolean isCorrectInstruction = dp.checkIfUpdatedRecordCorrect(strUpdatedMoe, strInstruction);
			Boolean isCorrectDuration = dp.checkIfUpdatedRecordCorrect(strUpdatedMoe, strDuration);
			shared_functions.do_screen_capture_with_filename(driver, "T01S05_4");
			if( isCorrectInstruction&&isCorrectDuration ){
				steps_passed = steps_passed + 1;
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_4", "update moe order - updated duration and instruction found");
				System.out.println("test_update_moe_order - verify QAG_checkpoint_4 update moe order - updated duration and instruction found");
			}else{
				shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_4", "update moe order - updated duration and instruction not found");
			}
		}
		dp.clickRemoveBtn();
		dp.confirmRemove();
		//Find drug
		//PIRITON (CHLORPHENIRAMINE MALEATE) tablet<BR>oral : 4 mg tds for 1 weeks
		//PARACETAMOL ALCOHOL FREE suspension<BR>oral : 500 mg q4h prn (100%) for 1 weeks
		int number_of_drugs = 2;
		int number_of_record_found = 0;
		for(int i=0; i<number_of_drugs; i++){
			suffix = "_d"+(i+1);
			str = dict.get("new_moe_check"+suffix); 
			List<WebElement> liMoes3 = dp.getMoeByDrugNameNDosage(str);
			if(liMoes3.size()>0){
				number_of_record_found = number_of_record_found + 1;
			}
		}
		//QAG_checkpoint_5
		shared_functions.do_screen_capture_with_filename(driver, "T01S05_5");
		if(number_of_record_found == (number_of_drugs-1)){
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_5", "update moe order - the updated item is not found");
			System.out.println("test_update_moe_order - verify QAG_checkpoint_5 update moe order - the updated item is not found");
		}else{
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_5", "update moe order - the updated item is found");
		}
		System.out.println("test_update_moe_order - END");
	}
	public void test_remove_order() throws Exception {
		System.out.println("test_remove_order - START");
		dp.clickDeleteOrderBtn();
		dp.confirmDelete();
		if( shared_functions.isAlertPresent(driver) ) {
			shared_functions.handleAlert(driver, false);
		}
		shared_functions.do_screen_capture_with_filename(driver, "T01S05_6");
		Boolean isNotExistIframe226Panel = dp.getIframe226Panel().size()==0;
		if(isNotExistIframe226Panel){
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_6", "del moe order - moe function is closed");
			System.out.println("test_remove_order - verify QAG_checkpoint_6 del moe order - moe function is closed");
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_6", "del moe order - moe function is not closed");
		}
		open_moe_function();
		dp.clickHistoryBtn();
		shared_functions.do_screen_capture_with_filename(driver, "T01S05_7");
		Boolean isExistCaseNoOnToday = dp.checkIfExistCaseNoOnToday(check_case_no,check_today_date);
		if(!isExistCaseNoOnToday){
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_7", "del moe order - the order not found in history screen");
			System.out.println("test_remove_order - verify QAG_checkpoint_7 del moe order - the order not found in history screen");
		}
		dp.clickCancelBtn();
		cmsMainPage.fnNextPatient();
		System.out.println("test_remove_order - END");
	}
	public void test_drugset() throws Exception {
		System.out.println("test_drugset - START");
		dp.clickDrugSetBtn();
		dp.inputDrugSet(dict.get("drugset_keywoard"));
		dp.inputDrugSetDuration(dict.get("drugset_duration"));
		dp.clickDrugSetSelectAllBtn();
		dp.clickDrugSetAddBtn();
		int drugs_found = 0;
		int drug_in_excel_file = 0;
		for(int i=0;i<99;i++){
			drug_in_excel_file = i;
			String k = "check_drugset_drug_" + (i+1); 
			Boolean isNull = (dict.get(k)==null);
			if(!isNull){
				String v = dict.get(k);
				String check_drug = v.replace("(w)", dict.get("drugset_duration")).replace("\\","");
				Boolean b = dp.getMoeByDrugNameNDosage(check_drug).size()>0;
				if(b){
					drugs_found = drugs_found + 1;
				}
			}else{
				break;
			}
		}
		shared_functions.do_screen_capture_with_filename(driver, "T01S05_8");
		System.out.println("drug_in_excel_file:"+drug_in_excel_file);
		System.out.println("drugs_found:"+drugs_found);
		if(drug_in_excel_file == drugs_found){
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_8", "add order from drugset - number of drugs match");
			System.out.println("test_remove_order - verify QAG_checkpoint_8 add order from drugset - number of drugs match");
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_8", "add order from drugset - number of drugs not match");
		}
		close_opmoe_without_save();
		System.out.println("test_drugset - END");
	}
	public void test_standard_regimen() throws Exception {
		System.out.println("test_standard_regimen - START");
		dp.clickStandardRegimenBtn();
		dp.inputStandardRegimen(dict.get("regimen_keyword"));
		dp.inputDrugSetDuration(dict.get("drugset_duration"));
		dp.clickDrugSetAddBtn();
		int drugs_found = 0;
		int drug_checked = 0;
		for(int i=0;i<99;i++){
			String k = "check_regimen_drug_" + (i+1); 
			Boolean isNull = (dict.get(k)==null);
			if(!isNull){
				drug_checked = drug_checked + 1;
				String v = dict.get(k);
				//PIRITON \(CHLORPHENIRAMINE MALEATE\) tablet<BR>oral : 4 mg tds for 1 weeks
				//PANADOL \(PARACETAMOL ALCOHOL FREE\) suspension<BR>oral : 500 mg q4h prn \(100%\) for 1 weeks
				//THEOPHYLLINE ALCOHOL FREE syrup<BR>oral : 10 mg daily for 1 weeks
				String check_drug = v.replace("\\","");
				Boolean b = dp.getMoeByDrugNameNDosage(check_drug).size()>0;
				if(b){
					drugs_found = drugs_found + 1;
					shared_functions.reporter_ReportEvent("micDone","standard_regimen drug check "+i,"found: "+check_drug);
				}
			}else{
				break;
			}
		}
		shared_functions.do_screen_capture_with_filename(driver, "T01S05_9");
		if(drug_checked == drugs_found){
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_9", "add item from standard regimen - number of drugs match");
			System.out.println("test_remove_order - verify QAG_checkpoint_9 add item from standard regimen - number of drugs match");
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_9", "add item from standard regimen - only "+drugs_found+"/"+drug_checked+" number of drugs match");
		}
		close_opmoe_without_save();
		System.out.println("test_standard_regimen - END");
	}
	
	class PAGE_PatientDetailPage_DischargePrescription{
		WebDriver driver = null;
		public PAGE_PatientDetailPage_DischargePrescription(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe(){
			driver.switchTo().defaultContent();
			driver.switchTo().frame("226Panel");			
		}
		//Check exist
		public Boolean isExistPrescription(){
			switchToIframe();
			String xp = "//label[contains(text(),'A prescription already exists for this patient.')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			return li.size()>0;
		}
		public Boolean isExistFailToRetrievePMSInformation(){
			switchToIframe();
			String xp = "//textarea[contains(text(),'Fail to retrieve PMS information')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			return li.size()>0;
		}
		public Boolean isExistFutureAppointmentDate(){
			switchToIframe();
			String xp = "//span[contains(text(),'Future Appointment Date')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			return li.size()>0;
		}
		public Boolean isExistPreviousPrescription(){
			switchToIframe();
			String xp = "//label[contains(text(),'Previous Prescription')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			return li.size()>0;
		}
		public Boolean checkIfExistImg(){
			switchToIframe();
			String css = "img.x-tree-ec-icon.x-tree-elbow-end-plus";
			List<WebElement> li = driver.findElements(By.cssSelector(css));
			if(li.size()>0){
				return true;
			}
			return false;
		}
		public Boolean checkIfExistNewRecord(String strAllRecord, String newRecord){
			return strAllRecord.substring(0, newRecord.length()).equals(newRecord);
		}
		public Boolean checkIfExistCaseNoOnToday(String case_no,String today_date){
			Boolean isExistToday = (dp.getContentOfPreviousPrescriptionTable().indexOf(check_today_date)>0);
			Boolean isExistCaseNo = (dp.getContentOfPreviousPrescriptionTable().indexOf(check_case_no)>0);
			return (isExistToday&&isExistCaseNo);
		}
		public Boolean checkIfUpdatedRecordCorrect(String strRecord, String strCheck){
			Boolean b = strRecord.indexOf(strCheck)>0;
			return b;
		}
		public Boolean isExistLeavingPrescriptionPopup(){
			Boolean b =false;
			switchToIframe();
			String xp = "//textarea[contains(text(),'You are leaving this prescription. Save the prescription first?')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				b=true;
			}
			return b;
		}
		public Boolean isExistSavePMSDataFailed(){
			switchToIframe();
			String xp = "//textarea[contains(text(),'Save pmsData failed')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			Boolean b = li.size()>0;
			System.out.println("isExistSavePMSDataFailed:"+b);
			return b;
		}
		//Click
		public void clickOKBtnForSavePMSDataFailed(){
			switchToIframe();
			String xp = "//span[contains(text(),'K')]/u[contains(text(),'O')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				System.out.println("clickOKBtnForSavePMSDataFailed");
				li.get(0).click();				
			}
		}
		public void closeFutureAppointmentDate(){
			switchToIframe();
			String xp = "//table[@id='faApplyBtn']";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();				
			}
		}
		public void clickCancelBtn() throws InterruptedException{
			System.out.println("clickCancelBtn");
			switchToIframe();
			String css = "div#cancelBtn";
			List<WebElement> li = driver.findElements(By.cssSelector(css));
			if(li.size()>0){
				li.get(0).click();
			}
			waitForCancelBtnHide();
		}
		public void waitForCancelBtnHide() throws InterruptedException{
			String css = "div#cancelBtn";
			waitForCssHide(css);
		}
		public void clickHistoryBtn() throws InterruptedException{
			switchToIframe();
			String xp = "//div[@id='moeHistoryBtn']";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();
			}
			waitForCancelBtn();
		}
		public void clickAddBtn() throws InterruptedException{
			String css = "table#moe-drugSearchBottomButton-table div#Addbutton";
			List<WebElement> li = driver.findElements(By.cssSelector(css));
			if(li.size()>0){
				li.get(0).click();
			}
			waitForAddBtnHide();
		}
		public void waitForAddBtnHide() throws InterruptedException{
			String css = "div#Addbutton";
			waitForCssHide(css);
		}
		public void clickSaveBtn() throws InterruptedException{
			String xp = "//span[contains(text(),'Save and')]//u[contains(text(),'P')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();
			}
			waitForSaveNPrintBtnDisable();
		}
		public void waitForSaveNPrintBtnDisable() throws InterruptedException{
			String css = "div#PIsapBtn>div";
			waitForCssDisable(css);
		}
		public void clickOkBtn(){
			switchToIframe();
			String xp = "//span[contains(text(),'K')]/u[contains(text(),'O')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();
			}
		}
		public void clickDeleteOrderBtn() throws InterruptedException{
			switchToIframe();
			String xp = "//span[contains(text(),'Delete')]//u[contains(text(),'O')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){li.get(0).click();}
		}
		public void confirmDelete() throws InterruptedException{
			String xp = "//textarea[contains(text(),'Delete the whole prescription')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				String xp2 = "//span[contains(text(),'es')]//u[contains(text(),'Y')]";
				List<WebElement> li2 = driver.findElements(By.xpath(xp2));
				if(li2.size()>0){
					li2.get(0).click();
				}
			}
			while(dp.getIframe226Panel().size()!=0){
				System.out.println("Still in Iframe226Panel");
			} 
		}
		public void inputStartDateDuration(String str){
			switchToIframe();
			String xp = "//legend[contains(text(),'Prescription Duration')]/following-sibling::table//td[contains(text(),'for')]/following-sibling::td[1]//input";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				shared_functions.clickAndType(li.get(0), str);
			}
		}
		public void inputDrugKeyword(String str){
			System.out.println("inputDrugKeyword:"+str);
			switchToIframe();
			String css = "input#moeDrugSearchTriggerText";
			List<WebElement> li = driver.findElements(By.cssSelector(css));
			if(li.size()>0){
				shared_functions.clickAndType(li.get(0), str);
			}
		}
		public void selectDrugName(String str){
			String xp = "//a[contains(text(),'"+str+"')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();
			}

		}
		public void selectDrugType(String str){
			String xp = "//div[contains(text(),'"+str+"') and @class='routeNode']";
			//String xp = "//div[contains(text(),'"+str+"') and contains(@class,'routeNode')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();
			}			
		}
		public void inputCommonDosage(String str){
			String xp = "//div[contains(text(),'"+str+"') and contains(@id,'moe-comDoseContent-')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();
			}			
		}
		public void clickEditBtn() throws InterruptedException{
			String xp = "//span[contains(text(),'dit')]//u[contains(text(),'E')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();
			}
			waitForAcceptBtnDisplay();
		}
		public void inputDuration(String str){
			System.out.print("inputDuration:");
			switchToIframe();
			String css = "div.moe-durationPanel input[class*='moe-ui-textfield']";
			List<WebElement> li = driver.findElements(By.cssSelector(css));
			System.out.println(li.size());
			if(li.size()>0){
				shared_functions.clickAndType(li.get(0),str);
				/*
				WebElement e = li.get(0);
				System.out.println("e:"+e);
				e.clear();
				System.out.println("e clear");
				e.click();
				System.out.println("e click");
				e.sendKeys(str);
				System.out.println("e sendKeys:"+str);
				e.sendKeys(Keys.TAB);
				System.out.println("e sendKeys ENTER");
				*/
				//WebElement e = li.get(0);
				//System.out.println("e:"+e);
				//e.clear();
				//System.out.println("e clear");
				//e.sendKeys(str);
				//System.out.println("e sendKeys:"+str);				
			}
		}
		public void inputSpecialInstruction(String str){
			System.out.print("inputSpecialInstruction:");
			switchToIframe();
			String css = "textarea#moe-edit-lower-specInstruct";
			List<WebElement> li = driver.findElements(By.cssSelector(css));
			System.out.println(li.size());
			if(li.size()>0){
				shared_functions.clickAndType(li.get(0),str);
				/*
				WebElement e = li.get(0);
				System.out.println("e:"+e);
				e.clear();
				System.out.println("e clear");
				e.click();
				System.out.println("e click");
				e.sendKeys(str);
				System.out.println("e sendKeys:"+str);
				e.sendKeys(Keys.ENTER);
				System.out.println("e sendKeys ENTER");
				*/
			}
		}
		public void clickAcceptBtn() throws InterruptedException{
			System.out.print("clickAcceptBtn size:");
			switchToIframe();
			String xp = "//span[contains(text(),'ccept')]//u[contains(text(),'A')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			System.out.println(li.size());
			if(li.size()>0){
				WebElement e = li.get(0);
				e.click();
				System.out.print("clickAcceptBtn-clicked:"+e);
			}
			waitForHistoryBtnEnable();
		}
		public void clickRemoveBtn(){		
			String xp = "//span[contains(text(),'emove')]//u[contains(text(),'R')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();
			}
		}
		public void confirmRemove(){
			String xp = "//textarea[contains(text(),'Remove the item?')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				xp = "//span[contains(text(),'es')]//u[contains(text(),'Y')]";
				li = driver.findElements(By.xpath(xp));
				if(li.size()>0){
					li.get(0).click();
				}
			}			
		}
		public void clickNoForSavePrescritionFirst(){
			switchToIframe();
			String xp = "//span[contains(text(),'o')]//u[contains(text(),'N')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();
			}
		}
		public void clickDrugSetBtn() throws InterruptedException{
			switchToIframe();
			String xp = "//span[contains(text(),'Dr')]//u[contains(text(),'u')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();
			}
			waitForDrugSetCancelBtnEnable();
			waitForDrugSetInputEnable();
		}
		public void inputDrugSet(String str) throws InterruptedException{
			switchToIframe();
			//String str = dict.get("drugset_keywoard");
			String css = "div#drugsetTextDiv input";
			List<WebElement> li = driver.findElements(By.cssSelector(css));
			if(li.size()>0){
				shared_functions.clickAndType(li.get(0), str);
			}
			waitToLoad();
		}
		public void inputDrugSetDuration(String str){
			String xp = "//div[@id='drugsetDurationPanel']//legend[contains(text(),'Prescription Duration')]/following-sibling::table//td[contains(text(),'for')]/following-sibling::td[1]//input";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				shared_functions.clickAndType(li.get(0), str);
			}
		}
		public void clickDrugSetSelectAllBtn(){
			String xp = "//div[@id='drugsetSelectAllBtn']//span[contains(text(),'elect All')]//u[contains(text(),'S')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			if(li.size()>0){
				li.get(0).click();
			}
		}
		public void clickDrugSetAddBtn() throws InterruptedException{
			String xp = "//div[@id='drugsetAddBtn']//span[contains(text(),'dd')]//u[contains(text(),'A')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			System.out.println("clickDrugSetAddBtn:"+li.size());
			if(li.size()>0){
				li.get(0).click();
			}
			waitForHistoryBtnEnable();
		}
		public void clickStandardRegimenBtn() throws InterruptedException{
			switchToIframe();
			String xp = "//span[contains(text(),'tandard Regimen')]//u[contains(text(),'S')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			System.out.println("clickStandardRegimenBtn:"+li.size());
			if(li.size()>0){
				li.get(0).click();
			}
			waitForDrugSetCancelBtnEnable();
			waitForStandardRegimenInputEnable();
		}
		public void inputStandardRegimen(String str) throws InterruptedException{
			switchToIframe();
			String css = "div#drugsetTextDiv input";
			List<WebElement> li = driver.findElements(By.cssSelector(css));
			System.out.println("inputStandardRegimen:"+li.size());
			if(li.size()>0){
				shared_functions.clickAndType(li.get(0), str);
			}
			waitToLoad();
		}
		
		public String getContentOfPreviousPrescriptionTable(){
			switchToIframe();				
			String xp = "//label[contains(text(),'Previous Prescription')]/ancestor::table[@class='x-table-layout']//table";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			String textOfPreviousPrescriptionTable = li.get(0).getText().replace("\n","").replace("\r","").replace("  "," ");
			return textOfPreviousPrescriptionTable;
		}
		public List<WebElement> getMoeByDrugNameNDosage(String str){
			String[] arrStr = str.split("<BR>");
			String xp = "//td[contains(text(),'"+arrStr[0]+"') or contains(text(),'"+arrStr[1]+"')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			return li;
		}
		public List<WebElement> getIframe226Panel(){
			driver.switchTo().defaultContent();
			String css = "iframe[name=226Panel]";
			List<WebElement> li = driver.findElements(By.cssSelector(css));
			return li;			
		}
		public List<WebElement> getMoeByDrugKeyword(String str){
			String xp = "//td[contains(text(),'"+str+"')]";
			List<WebElement> li = driver.findElements(By.xpath(xp));
			return li;
		}
		//wait
		public void waitForHistoryBtnDisplay() throws InterruptedException{
			String css = "td#historyBtnTD div#moeHistoryBtn";
			waitForCssDisplay(css);
		}
		public void waitForCancelBtn() throws InterruptedException{
			String css = "div#cancelBtn div#historyClose";
			waitForCssDisplay(css);
		}
		public void waitForHistoryBtnEnable() throws InterruptedException{
			String css = "td#historyBtnTD div#moeHistoryBtn";
			waitForCssEnable(css);
		}
		public void waitForSaveNPrintBtnDisplay() throws InterruptedException{
			String css = "div#PIsapBtn>div";
			waitForCssDisplay(css);
		}

		public void waitForAcceptBtnDisplay() throws InterruptedException{
			String css = "td#moeEditAcceptBtnTd";
			waitForCssDisplay(css);
		}
		public void waitForDrugSetCancelBtnEnable() throws InterruptedException{
			String css = "div#drugsetCancelBtn div#dsCancelBtn";
			waitForCssEnable(css);
		}
		public void waitForDrugSetInputEnable() throws InterruptedException{
			String css = "div#drugsetTextDiv input";
			waitForCssEnable(css);
		}
		public void waitForStandardRegimenInputEnable() throws InterruptedException{
			String css = "div#drugsetTextDiv input";
			waitForCssEnable(css);
		}
		public void waitToLoad() throws InterruptedException{
			System.out.print("waitToLoad, ");
			shared_functions.sleepForAWhile(500);
		}
		public void waitForCssHide(String css){
			Boolean bHide = false;
			while(!bHide){
				List<WebElement> li = driver.findElements(By.cssSelector(css));
				if(li.size()>0){
					bHide = !li.get(0).isDisplayed(); //display, break loop
				}else{
					bHide = false;
				}
				System.out.println("wait for css["+css+"] to hide:"+bHide);
			}
		}
		public void waitForCssDisplay(String css){
			Boolean bDisplay = false;
			while(!bDisplay){
				List<WebElement> li = driver.findElements(By.cssSelector(css));
				if(li.size()>0){
					bDisplay = li.get(0).isDisplayed(); //display, break loop
				}else{
					bDisplay = false;
				}
				System.out.println("wait for css["+css+"] to display:"+bDisplay);
			}
		}
		public void waitForCssEnable(String css){
			Boolean bEnable = false;
			while(!bEnable){
				List<WebElement> li = driver.findElements(By.cssSelector(css));
				if(li.size()>0){
					bEnable = li.get(0).isEnabled(); //enable, break loop
				}else{
					bEnable = false;
				}
				System.out.println("wait for css["+css+"] to enable:"+bEnable);
			}
		}
		public void waitForCssDisable(String css){
			Boolean bDisable = false;
			while(!bDisable){//disable, break loop
				List<WebElement> li = driver.findElements(By.cssSelector(css));
				if(li.size()>0){
					if(li.get(0).getAttribute("disabled") != null){
						bDisable = li.get(0).getAttribute("disabled").equals("true");
					}
				}else{
					bDisable = false;
				}
				System.out.println("wait for css["+css+"] to disable:"+bDisable);
			}
		}

	}
	
}
