package suite;

import java.util.ArrayList;
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

import suite.T05S01_bed_assignment.PAGE_PatientDetailPage_BedAssignment;

public class T05S04_discharge {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_BedAssignment bedAssignment = null;
	PAGE_PatientDetailPage_PMI pmi = null;
	PAGE_PatientDetailPage_Discharge discharge = null; 
	int steps_passed = 0;
	int total_steps = 7;
	WebElement eDischargeCodeDrpBx = null;
	WebElement eRemarkTextArea = null;
	Boolean isExistSelectDoctor = null;
	WebElement eSave = null;
	String check_case_no = null;
	String check_today_str = null;
	String check_case_no2 = null;
	String hosp_code = null;
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T05S04_discharge";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T05S04");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		discharge = new PAGE_PatientDetailPage_Discharge(driver);
		pmi = new PAGE_PatientDetailPage_PMI(driver);
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		test_discharge_normal();
		test_discharge_death();
		cmsMainPage.fnNextPatient();
		shared_functions.countTestPassed(steps_passed, total_steps);
	}
	public void test_discharge_normal() throws Exception{
		System.out.println("test_discharge_normal() - START");
		System.out.println("test_discharge_normal() - discharge HN080000251 WONG,MUI");
		cmsMainPage.fnDischarge();
		cmsMainPage.changeWardForNormalPatientList( dict.get("ward") );
		cmsMainPage.selectPatientByCaseNum( dict.get("discharge_normal_case_no") ); //HN080000251 WONG,MUI
		psf.closeExistingAlertReminderWindow();
		discharge.clearPreviousDischargeInfo();
		eDischargeCodeDrpBx = discharge.getDischargeCodeDrpBx();
		eDischargeCodeDrpBx.click();
		shared_functions.clearAndSend(eDischargeCodeDrpBx, dict.get("discharge_code")); //3
		eRemarkTextArea = discharge.getRemarkTextArea();
		shared_functions.clearAndSend(eRemarkTextArea, "[NORMAL]: "+dict.get("discharge_remarks")); //[NORMAL]: remarks by QTP
		isExistSelectDoctor = dict.containsKey("select_doctor");
		if(isExistSelectDoctor) {
			WebElement eDoctor = discharge.getDoctorDrpBx();
			shared_functions.clearAndSend(eDoctor, dict.get("select_doctor"));
		}
		eSave = discharge.getSaveBtn();
		eSave.click();
		discharge.handleDivisionTeamChoicePopUpWindow();
		System.out.println("test_discharge_normal() - verify case no not appear");
		cmsMainPage.getRefreshBtn().click();
		check_case_no = shared_functions.convert_case_no_to_cms_format(dict.get("discharge_normal_case_no"));
		shared_functions.do_screen_capture_with_filename(driver, "T05S04_1");
		Boolean isNotExistCaseNo = (cmsMainPage.getPatientIdxByCaseNum(check_case_no)==-1);
		if(isNotExistCaseNo) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "discharge normal - after discharge, case no# not found in PSP");
			steps_passed +=1;
		}
		cmsMainPage.fnNextPatient();
		System.out.println("test_discharge_normal() - verify unable to discharge a discharged case no");
		cmsMainPage.fnDischarge();
		cmsMainPage.changeWardForNormalPatientList( dict.get("ward") );
		cmsMainPage.selectPatientByCaseNum( dict.get("discharge_normal_case_no") ); //HN080000251 WONG,MUI
		psf.closeExistingAlertReminderWindow();
		shared_functions.do_screen_capture_with_filename(driver, "T05S04_2");
		Boolean isExist2 = discharge.checkIfDischargedByCaseNum(dict.get("discharge_normal_case_no"));
		if(isExist2) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "discharge normal - discharge a 'discharged person' shows expected warning");
			steps_passed = steps_passed + 1;
			WebElement eOk = cmsMainPage.getOKBtnInPopUpWindow();
			eOk.click();
		}
		cmsMainPage.fnNextPatient();
		System.out.println("test_discharge_normal() - verify PMI, last ward, discharge destination discharge remark");		
		cmsMainPage.fnPMI();
		cmsMainPage.changeWardForNormalPatientList( dict.get("ward") );
		cmsMainPage.selectPatientByCaseNum( dict.get("discharge_normal_case_no") ); //HN080000251 WONG,MUI
		psf.closeExistingAlertReminderWindow(); 
		shared_functions.do_screen_capture_with_filename(driver, "T05S04_3");
		check_today_str=shared_functions.getDateInddMMMyyyy();
		check_case_no2=shared_functions.convert_case_no_to_cms_format(dict.get("discharge_normal_case_no"));
		hosp_code = discharge.getHospCode();
		List<ArrayList<String>> tableEpisode = pmi.getTableEpisodeCaseList();
        Boolean isCorrectHospCode = tableEpisode.get(0).get(1).equals(hosp_code);
        Boolean isCorrectCaseNo = tableEpisode.get(0).get(3).equals(check_case_no2);
        Boolean isCorrectDischargeDateTime = tableEpisode.get(0).get(6).substring(0, 11).equals(check_today_str);
        Boolean isCorrectDestination = tableEpisode.get(0).get(7).equals("HOME");
        Boolean isCorrectWard = tableEpisode.get(0).get(8).equals(dict.get("ward"));
        Boolean isCorrectRemark = tableEpisode.get(0).get(12).equals("[NORMAL]: "+dict.get("discharge_remarks"));
        if(isCorrectHospCode&&isCorrectCaseNo&&isCorrectDischargeDateTime&&isCorrectDestination&&isCorrectWard&&isCorrectRemark) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "discharge normal - PMI shows the case no# has been discharged");
			steps_passed = steps_passed + 1;
        }
        cmsMainPage.fnNextPatient();
        System.out.println("test_discharge_normal() - END");
	}
	public void test_discharge_death() throws Exception{
		System.out.println("test_discharge_death() - START");
		cmsMainPage.fnDischarge();
		cmsMainPage.changeWardForNormalPatientList( dict.get("ward") );
		cmsMainPage.selectPatientByCaseNum( dict.get("discharge_death_case_no") ); //HN08000032T 
		psf.closeExistingAlertReminderWindow();
		discharge.clearPreviousDischargeInfo();
		eDischargeCodeDrpBx = discharge.getDischargeCodeDrpBx();
		shared_functions.clearAndSend(eDischargeCodeDrpBx, "1");
		WebElement eCertifiedDeathDate = discharge.getCertifiedDeathDate();
		shared_functions.clearAndSend(eCertifiedDeathDate, shared_functions.getDateInddMMMyyyy());		
		WebElement eCertifiedDeathTime = discharge.getCertifiedDeathTime();
		eCertifiedDeathTime.clear();
		eCertifiedDeathTime.sendKeys(Keys.HOME);
		eCertifiedDeathTime.sendKeys("0700");
		eCertifiedDeathTime.sendKeys(Keys.ENTER);
		eRemarkTextArea = discharge.getRemarkTextArea();
		shared_functions.clearAndSend(eRemarkTextArea, "[DEATH]: "+dict.get("discharge_remarks") );
		isExistSelectDoctor = dict.containsKey("select_doctor");
		if(isExistSelectDoctor) {
			WebElement eDoctor = discharge.getDoctorDrpBx();
			shared_functions.clearAndSend(eDoctor, dict.get("select_doctor"));
		}
		eSave = discharge.getSaveBtn();
		eSave.click();
		discharge.handleDivisionTeamChoicePopUpWindow();
		List<String> arrListWindowTitles = new ArrayList<>();
		arrListWindowTitles.add("Question (1-2100-28-Q-243)");
		arrListWindowTitles.add("Question (1-2100-28-Q-247)");
		arrListWindowTitles.add("Question / Action (1-2100-28-Q-243)");
		arrListWindowTitles.add("Question / Action (1-2100-28-Q-247)");
		Boolean isExistPopUpWindow =false;
		for(String s: arrListWindowTitles) {
			Boolean b = discharge.checkIfExistsPopUpWindow(s);
			System.out.println(s+": "+b);
			if(b) {
				isExistPopUpWindow =true;
				break;
			}
		}
		if(isExistPopUpWindow) {
			discharge.getYesBtn().click();
			psf.closeExistingAlertReminderWindow();
			discharge.getLastOfficeFormRetrieveBtn().click();
			discharge.getLastOfficeFormSaveNPrintBtn().click();
			shared_functions.do_screen_capture_with_filename(driver, "T05S04_4A_last_office_form");
			String strWindow = "//span[contains(text(),'Information (1-2100-28-I-261)')]";
			if(driver.findElements(By.xpath(strWindow)).size()>0) {
				shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_7", "Last Office Form - Mental Health Ordinance (MHO) status from PsyCIS is not available");
			}else {
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_7", "Last Office Form - Mental Health Ordinance (MHO) status from PsyCIS is available");
				steps_passed = steps_passed + 1;
			}
			discharge.getLastOfficeFormCancelBtn().click();
		}else {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_7", "Last Office Form - no last office form alert");
		}
		
		System.out.println("test_discharge_death() - Discharged case no should not appear ");
		shared_functions.do_screen_capture_with_filename(driver, "T05S04_4");
		check_case_no = shared_functions.convert_case_no_to_cms_format(dict.get("discharge_death_case_no"));
		int idxOfPatient = cmsMainPage.getPatientIdxByCaseNum(check_case_no);
		if( idxOfPatient==-1 ) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_4", "discharge death - after discharge, case no# not found in PSP");
			steps_passed = steps_passed + 1;
		}
		cmsMainPage.fnNextPatient();
		System.out.println("test_discharge_death() - verify dead warning is shown");
		cmsMainPage.fnPMI();
		cmsMainPage.selectPatientByCaseNum(dict.get("discharge_death_case_no"));
		psf.closeExistingAlertReminderWindow();
		shared_functions.do_screen_capture_with_filename(driver, "T05S04_5");
		String xp = "//textarea[contains(text(),'Patient has already been recorded dead on "+shared_functions.getDateInddMMMyyyy()+".')]";
		if( driver.findElements(By.xpath(xp)).size()>0 ) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_5", "discharge death - 'patient already dead' warning is shown when open function");
			steps_passed = steps_passed + 1;
			WebElement eOk = cmsMainPage.getOKBtnInPopUpWindow();
			eOk.click();
		}
		System.out.println("test_discharge_death() - verify PMI, last ward, discharge destination discharge remark");
		shared_functions.do_screen_capture_with_filename(driver, "T05S04_6");
		check_today_str = shared_functions.getDateInddMMMyyyy();
		check_case_no2 = shared_functions.convert_case_no_to_cms_format(dict.get("discharge_death_case_no"));
		hosp_code = discharge.getHospCode();
		List<ArrayList<String>> tableEpisodeForDead = pmi.getTableEpisodeCaseList();
        Boolean isCorrectHospCodeForDead = tableEpisodeForDead.get(0).get(1).equals(hosp_code);
        Boolean isCorrectCaseNoForDead = tableEpisodeForDead.get(0).get(3).equals(check_case_no2);
        Boolean isCorrectDischargeDateTimeForDead = tableEpisodeForDead.get(0).get(6).substring(0, 11).equals(check_today_str);
        Boolean isCorrectDestinationForDead = tableEpisodeForDead.get(0).get(7).equals("DEATH");
        Boolean isCorrectWardForDead = tableEpisodeForDead.get(0).get(8).equals(dict.get("ward"));
        Boolean isCorrectRemarkForDead = tableEpisodeForDead.get(0).get(12).equals("[DEATH]: "+dict.get("discharge_remarks"));
        if(isCorrectHospCodeForDead&&isCorrectCaseNoForDead&&isCorrectDischargeDateTimeForDead&&isCorrectDestinationForDead&&isCorrectWardForDead&&isCorrectRemarkForDead) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_6", "discharge death - PMI shows the case no# has been discharged death");
			steps_passed = steps_passed + 1;
        }    
        System.out.println("test_discharge_death() - END");
	}
	
	class PAGE_PatientDetailPage_Discharge {
		WebDriver driver = null;
		public PAGE_PatientDetailPage_Discharge(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframeDischarge(){
			driver.switchTo().defaultContent();
			driver.switchTo().frame("79Panel");			
		}
		public void clearPreviousDischargeInfo() {
			switchToIframeDischarge();
			String strPreviousDischargeInfoPopUpWindow = "Previous discharge information exists, please check carefully.";
			String xpPreviousDischargeInfoPopUpWindow = "//span[contains(text(),'"+strPreviousDischargeInfoPopUpWindow+"')]";
			Boolean isExistPreviousDischargeInfo = driver.findElements(By.xpath(xpPreviousDischargeInfoPopUpWindow)).size()>0;
			if(isExistPreviousDischargeInfo) {
				String strBtnsOfPopUpWindow = "div.x-window-body div.x-container a.x-btn table.x-table-plain td.x-frame-mc span.x-btn-inner";
				List<WebElement> liBtnsOfPopUpWindow = driver.findElements(By.cssSelector(strBtnsOfPopUpWindow));
				WebElement eClearAllDetails = liBtnsOfPopUpWindow.get(2);
				if(eClearAllDetails.isDisplayed()) {
					eClearAllDetails.click();
				}else {
					shared_functions.clickNonDisplayedElement(driver, eClearAllDetails);				
				}
			}
		}
		public WebElement getDischargeCodeDrpBx() {
			switchToIframeDischarge();
			String str = "#id_cbx_discharge_code-inputEl";
			WebElement e = driver.findElement(By.cssSelector(str));
			return e;
		}
		public WebElement getCertifiedDeathDate() {
			switchToIframeDischarge();
			String str = "#id_pasdf_death_date_parent-inputEl";
			WebElement e = driver.findElement(By.cssSelector(str));
			return e;
		}
		public WebElement getCertifiedDeathTime() {
			switchToIframeDischarge();
			String str = "#id_tmf_death_time_parent-inputEl";
			WebElement e = driver.findElement(By.cssSelector(str));
			return e;
		}
		public WebElement getRemarkTextArea() {
			switchToIframeDischarge();
			String str = "#id_txt_remark-inputEl";
			WebElement e = driver.findElement(By.cssSelector(str));
			return e;			
		}
		public WebElement getDoctorDrpBx() {
			switchToIframeDischarge();
			String str = "#id_cbx_doctor-inputEl";
			WebElement e = driver.findElement(By.cssSelector(str));
			return e;			
		}
		public WebElement getSaveBtn() {
			switchToIframeDischarge();
			String str = "Save";
			WebElement e = driver.findElement(By.xpath("//span[contains(text(),'"+str+"')]"));
			return e;			
		}
		public WebElement getYesBtn() {
			switchToIframeDischarge();
			String xp = "//span[contains(text(),'es')]//u[contains(text(),'Y')]";
			WebElement e = driver.findElement(By.xpath(xp));
			return e;
		}
		public void handleDivisionTeamChoicePopUpWindow() {
			System.out.println("handleDivisionTeamChoicePopUpWindow-START");
			switchToIframeDischarge();
			String strWinTitle = "Question / Action (1-103-1-Q-010)";
			if(checkIfExistsPopUpWindow(strWinTitle)) {
				String str = "No";
				WebElement eNo = driver.findElement(By.xpath("//span[contains(text(),'"+str+"')]"));
				eNo.click();
			}
			System.out.println("handleDivisionTeamChoicePopUpWindow-END");
		}
		public Boolean checkIfExistsPopUpWindow(String windowTitle) {
			System.out.println("checkIfExistsPopUpWindow-START:"+windowTitle);
			switchToIframeDischarge();
			Boolean isExist = false; 
			String xp = "//span[contains(text(),'"+windowTitle+"')]";
			if( driver.findElements(By.xpath(xp)).size()>0 ) {
				isExist=true;
			}
			System.out.println("checkIfExistsPopUpWindow-END");
			return isExist;			
		}
		public Boolean checkIfDischargedByCaseNum(String case_no) {
			switchToIframeDischarge();
			String str = "The patient  "+case_no+" has been discharged";
			Boolean b = driver.findElements(By.xpath("//textarea[contains(text(),'"+str+"')]")).size()>0;
			return b;
		}	
		public String getHospCode(){
			Boolean b = shared_functions.Parameter.get("hospital_code").equals("VH3");
			String hosp_code = null;
			if(b){
				hosp_code = shared_functions.Parameter.get("hospital_code").substring(0, 2);
			}else {
				hosp_code = shared_functions.Parameter.get("hospital_code");
			}
			return hosp_code;
		}
		/*public void switchToIframeLastOfficeForm_toBeDelete(){
			driver.switchTo().defaultContent();
			driver.switchTo().frame(8);
		}*/
		public void switchToIframeLastOfficeForm(){
			driver.switchTo().defaultContent();
			int numOfIFrame = driver.findElements(By.cssSelector("iframe")).size();
			//for(int i=numOfIFrame-1; i>=0; i--){
			for(int i=0; i<numOfIFrame; i++){
				System.out.println("switchToIframeLastOfficeForm["+i+"]");
				driver.switchTo().frame(i);
				String str = "#bn_dod_retrieve-btnInnerEl";
				Boolean isCorrectIframe = driver.findElements(By.cssSelector(str)).size()>0;
				if(isCorrectIframe){
					System.out.println("correct-switchToIframeLastOfficeForm["+i+"]");
					break;
				}
			}
		}
		public WebElement getLastOfficeFormRetrieveBtn() {
			switchToIframeLastOfficeForm();
			String str = "#bn_dod_retrieve-btnInnerEl";
			WebElement e = driver.findElement(By.cssSelector(str));
			return e;
		}
		public WebElement getLastOfficeFormSaveNPrintBtn() {
			String str = "#btnSaveAndPrint-btnInnerEl";
			WebElement e = driver.findElement(By.cssSelector(str));
			return e;			
		}
		public WebElement getLastOfficeFormCancelBtn() {
			String str = "#btnClose-btnInnerEl";
			WebElement e = driver.findElement(By.cssSelector(str));
			return e;			
		}
	}
	
}
