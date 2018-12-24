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

import suite.T05S01_bed_assignment.PAGE_PatientDetailPage_BedAssignment;
import suite.T05S04_discharge.PAGE_PatientDetailPage_Discharge;

public class T05S05_modification_of_transfer {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_ModificationOfTransfer modOfTransfer = null; 
	int steps_passed = 0;
	int total_steps = 4;
	int psp_category_col = 3;
	int psp_specialty_col = 2;
	int psp_case_no_col = 1;

	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T05S05_modification_of_transfer";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T05S05");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		modOfTransfer = new PAGE_PatientDetailPage_ModificationOfTransfer(driver); 
		//driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		test_modify_category();
		test_modify_specialty();
		test_modify_ward();
		cmsMainPage.fnNextPatient();
		shared_functions.countTestPassed(steps_passed, total_steps);
	}
	public void test_modify_category() throws Exception{
		System.out.println("test_modify_category() - START HN080000642 LI,HOI TONG");
		String case_no = dict.get("transfer_category_case_no"); //HN080000642
		String html_id_category = dict.get("html_id_category");//ext-comp-1013-inputEl
		cmsMainPage.changeWardForNormalPatientList( dict.get("ward") ); //8A
		cmsMainPage.fnModificationOfTransfer();
		cmsMainPage.selectPatientByCaseNum(case_no); 
		psf.closeExistingAlertReminderWindow();
		System.out.println("test_modify_category() - check different 'previous_category' & 'modified_category'");
		String previous_category = modOfTransfer.getFieldById(html_id_category).getAttribute("value");
		String modified_category = dict.get("modified_category"); //1
		if( previous_category.equals( modified_category ) ) {
			shared_functions.reporter_ReportEvent("micFail", "QAG_failure", "modification of transfer - same category");
			cmsMainPage.fnNextPatient();
			//Exit Function test_modify_category
		}
		System.out.println("test_modify_category() - modify category 2->1"); 
		WebElement eCategory = modOfTransfer.getFieldById(html_id_category);
		shared_functions.clearAndSend(eCategory, modified_category );//1
		modOfTransfer.getSaveBtn().click();
		modOfTransfer.handlePopUpWindow();		
		cmsMainPage.fnNextPatient();
		System.out.println("test_modify_category() - verify category is modified");
		shared_functions.do_screen_capture_with_filename(driver, "T05S05_1");
		String check_case_no = shared_functions.convert_case_no_to_cms_format(case_no);
		String strCategory = cmsMainPage.getCategoryByCaseNum(check_case_no);
		if(strCategory.equals(modified_category)) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "modification of transfer - modified category found in PSP");	
			steps_passed = steps_passed + 1;
		}
		cmsMainPage.fnNextPatient();
		System.out.println("test_modify_category() - END");
	}
	public void test_modify_specialty() throws Exception{
		System.out.println("test_modify_specialty() - START HN08000026Z LEUNG,APPLE");
		String case_no2 = dict.get("transfer_specialty_case_no"); //HN08000026Z
		String html_id_specialty = dict.get("html_id_specialty");//ext-comp-1015-inputEl
		cmsMainPage.changeWardForNormalPatientList( dict.get("ward") ); //8A
		cmsMainPage.fnModificationOfTransfer();
		cmsMainPage.selectPatientByCaseNum(case_no2); 
		psf.closeExistingAlertReminderWindow();
		System.out.println("test_modify_specialty() - check different 'previous_specialty' & 'modified_specialty'");
		String previous_specialty = modOfTransfer.getFieldById(html_id_specialty).getAttribute("value");
		String modified_specialty = dict.get("modified_specialty"); //NUR
		if( previous_specialty.equals( modified_specialty ) ) {
			shared_functions.reporter_ReportEvent("micFail", "QAG_failure", "modification of transfer same specialty");
			cmsMainPage.fnNextPatient();
			//Exit Function test_modify_specialty
		}
		System.out.println("test_modify_specialty() - modify specialty SUR->NUR"); 
		WebElement eSpecialty = modOfTransfer.getFieldById(html_id_specialty);
		shared_functions.clearAndSend(eSpecialty, modified_specialty);
		modOfTransfer.getSaveBtn().click();
		modOfTransfer.handlePopUpWindow();
		cmsMainPage.fnNextPatient();
		System.out.println("test_modify_specialty() - verify specialty is modified");
		shared_functions.do_screen_capture_with_filename(driver, "T05S05_2");
		String check_case_no2 = shared_functions.convert_case_no_to_cms_format(case_no2);
		String strSpecialty = cmsMainPage.getSpecialtyByCaseNum(check_case_no2);
		if(strSpecialty.equals(modified_specialty)) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "modification of transfer - modified specialty found in PSP");	
			steps_passed = steps_passed + 1;
		}
		cmsMainPage.fnNextPatient();
		System.out.println("test_modify_specialty() - END");
	}
	public void test_modify_ward() throws Exception{
		System.out.println("test_modify_ward() - START HN08000013X KONG,WING JAK");
		String case_no3 = dict.get("transfer_ward_case_no"); //HN08000013X
		String html_id_ward = dict.get("html_id_ward");//ext-comp-1011-inputEl
		cmsMainPage.changeWardForNormalPatientList( dict.get("ward") ); //8A
		cmsMainPage.fnModificationOfTransfer();
		cmsMainPage.selectPatientByCaseNum(case_no3);
		psf.closeExistingAlertReminderWindow();
		System.out.println("test_modify_ward() - check different 'previous_ward' & 'modified_ward'");
		String previous_ward = modOfTransfer.getFieldById(html_id_ward).getAttribute("value"); //7B
		String modified_ward = dict.get("modified_ward"); //7A
		if( previous_ward.equals( modified_ward ) ) {
			shared_functions.reporter_ReportEvent("micFail", "QAG_failure", "modification of transfer - same ward");
			cmsMainPage.fnNextPatient();
			//Exit Function test_modify_specialty
		}
		System.out.println("test_modify_ward() - modify ward 7B->7A");
		WebElement eWard = modOfTransfer.getFieldById(html_id_ward);
		shared_functions.clearAndSend(eWard, modified_ward);
		modOfTransfer.getSaveBtn().click();
		modOfTransfer.handlePopUpWindow();
		cmsMainPage.fnNextPatient();
		
		System.out.println("test_modify_ward() - verify case no# did not appear in 'previous' ward");
		cmsMainPage.changeWardForNormalPatientList(previous_ward); //7B
		shared_functions.do_screen_capture_with_filename(driver, "T05S05_3");
		String check_case_no3 = shared_functions.convert_case_no_to_cms_format(case_no3);
		if(cmsMainPage.getPatientIdxByCaseNum(check_case_no3)==-1) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "modification of transfer - case no# did not appear in 'previous' ward");
			steps_passed = steps_passed + 1;
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_3", "modification of transfer - case no# still appear in 'previous' ward");
		}
		
		System.out.println("test_modify_ward() - verify case no# appear in 'modified' ward");
		cmsMainPage.changeWardForNormalPatientList(modified_ward); //7A
		shared_functions.do_screen_capture_with_filename(driver, "T05S05_4");
		check_case_no3 = shared_functions.convert_case_no_to_cms_format(case_no3);
		if(cmsMainPage.getPatientIdxByCaseNum(check_case_no3)> -1) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_4", "modification of transfer - case no# found in 'modified' ward");
			steps_passed = steps_passed + 1;
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_4", "modification of transfer - case no# not found in 'modified' ward");
		}
		System.out.println("test_modify_ward() - END");
	}
	
	class PAGE_PatientDetailPage_ModificationOfTransfer {
		WebDriver driver = null;
		public PAGE_PatientDetailPage_ModificationOfTransfer(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe() {
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("81Panel");
		}
		public void changeFieldById(String id, String strNewText) {
			switchToIframe();
			String css = "#ext-comp-"+id+"-inputEl";//ext-comp-{id}-inputEl
			List<WebElement> li = shared_functions.getElementsWhenVisible(By.cssSelector( css ));
			WebElement e = li.get(0);
			shared_functions.clearAndSend(e, strNewText);
		}
		public void handlePopUpWindow() {
			String str = "Question / Action (1-112-1-Q-003)";
			Boolean b = shared_functions.checkAndGetElementsWhenVisible(By.xpath("//span[contains(text(),'"+str+"')]"))!=null;
			if(b) {
				System.out.println("PAGE_PatientDetailPage_ModificationOfTransfer PopUpWindow is exist");
				this.getNoBtn().click();
			}else {
				System.out.println("PAGE_PatientDetailPage_ModificationOfTransfer PopUpWindow is not exist");
			}
		}
		public WebElement getFieldById(String id) {
			switchToIframe();
			String css = "#ext-comp-"+id+"-inputEl";//ext-comp-{id}-inputEl
			List<WebElement> li = shared_functions.getElementsWhenVisible(By.cssSelector( css ));
			WebElement e = li.get(0);
			return e;
		}		
		public WebElement getSaveBtn() {
			String str = "Save";
			WebElement e = shared_functions.getElementWhenClickable(By.xpath("//span[contains(text(),'"+str+"')]"));
			return e;			
		}
		public WebElement getOKBtn() {
			String xp = "//span[contains(text(),'K')]//u[contains(text(),'O')]";
			WebElement e = shared_functions.getElementWhenClickable(By.xpath(xp));
			return e;
		}
		public WebElement getYesBtn() {
			String xp = "//span[contains(text(),'es')]//u[contains(text(),'Y')]";
			WebElement e = shared_functions.getElementWhenClickable(By.xpath(xp));
			return e;
		}
		public WebElement getNoBtn() {
			String xp = "//span[contains(text(),'o')]//u[contains(text(),'N')]";
			WebElement e = shared_functions.getElementWhenClickable(By.xpath(xp));
			return e;
		}
	}
}
