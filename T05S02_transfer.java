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

import suite.T05S06_modification_of_discharge.PAGE_PatientDetailPage_ModificationOfDischarge;

public class T05S02_transfer {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_Transfer transfer = null;
	int steps_passed = 0;
	int total_steps = 4;
	int psp_category_col = 3;
	int psp_specialty_col = 2;
	int psp_case_no_col = 1;
	Boolean isSameText = false;
	String strInExcel = null;
	String strInCMS = null;
	String caseNum = null;
	String current_ward = null;
	String idField = null;
	WebElement eField = null;
	String strField = null;
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T05S02_transfer";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T05S02");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		transfer = new PAGE_PatientDetailPage_Transfer(driver);
		current_ward = dict.get("ward"); //8A
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		test_transfer_category();
		test_transfer_specialty();
		test_transfer_ward();
		cmsMainPage.fnNextPatient();
		shared_functions.countTestPassed(steps_passed, total_steps);		
	}
	public void test_transfer_category() throws Exception{
		System.out.println("test_transfer_category() - START HN080000642 LI,HOI TONG");
		cmsMainPage.changeWardForNormalPatientList(current_ward);
		cmsMainPage.fnTransfer();
		cmsMainPage.selectPatientByCaseNum(dict.get("transfer_category_case_no"));  //HN080000642
		System.out.println("test_transfer_category() - change category 3->2");
		idField = dict.get("html_id_category"); //1013
		eField = transfer.getFieldById(idField);
		strField = dict.get("transfer_to_category"); //2
		shared_functions.clearAndSend(eField, strField);
		transfer.clickSaveBtn();
		System.out.println("test_transfer_category() - verify category is modified");
		shared_functions.do_screen_capture_with_filename(driver, "T05S02_1");
		caseNum = shared_functions.convert_case_no_to_cms_format(dict.get("transfer_category_case_no"));
		strInCMS = cmsMainPage.getCategoryByCaseNum(caseNum);
		strInExcel = dict.get("transfer_to_category");
		isSameText = (strInCMS.equals(strInExcel));
		if(isSameText) {
			steps_passed = steps_passed + 1	;
			shared_functions.reporter_ReportEvent("micPass","QAG_checkpoint_1", "transfer category - after transfer, category match expected in psp list");
		}
		cmsMainPage.fnNextPatient();
		System.out.println("test_transfer_category() - END");
	}
	public void test_transfer_specialty() throws Exception{
		System.out.println("test_transfer_specialty() - START HN08000026Z LEUNG,APPLE");
		cmsMainPage.fnTransfer();
		cmsMainPage.selectPatientByCaseNum(dict.get("transfer_specialty_case_no")); //HN08000026Z
		System.out.println("test_transfer_specialty() - change specialty GYN->SUR");
		idField = dict.get("html_id_specialty"); //1015
		eField = transfer.getFieldById(idField);
		strField = dict.get("transfer_to_specialty"); //SUR
		shared_functions.clearAndSend(eField, strField);
		transfer.clickSaveBtn();
		System.out.println("test_transfer_specialty() -  verify specialty is modified");
		shared_functions.do_screen_capture_with_filename(driver, "T05S02_2");
		caseNum = shared_functions.convert_case_no_to_cms_format(dict.get("transfer_specialty_case_no"));
		strInCMS = cmsMainPage.getSpecialtyByCaseNum(caseNum);
		strInExcel = dict.get("transfer_to_specialty");
		isSameText = (strInCMS.equals(strInExcel));
		if(isSameText) {
			steps_passed = steps_passed + 1	;
			shared_functions.reporter_ReportEvent("micPass","QAG_checkpoint_2", "transfer specialty - after transfer, specialty match expected in psp list");
		}
		cmsMainPage.fnNextPatient();
		System.out.println("test_transfer_specialty() - END");
	}
	public void test_transfer_ward() throws Exception{
		System.out.println("test_transfer_ward() - START HN08000013X KONG,WING JAK");
		cmsMainPage.fnTransfer();
		cmsMainPage.selectPatientByCaseNum(dict.get("transfer_ward_case_no")); //HN08000013X
		psf.closeExistingAlertReminderWindow();
		System.out.println("test_transfer_ward() - change ward 8A->7B");
		idField = dict.get("html_id_ward"); //1011
		eField = transfer.getFieldById(idField);
		strField = dict.get("transfer_to_ward"); //7B
		shared_functions.clearAndSend(eField, strField);
		transfer.clickSaveBtn();
		System.out.println("test_transfer_ward() -  verify no case no in 'transfer from' ward");
		cmsMainPage.changeWardForNormalPatientList(current_ward);//8A
		shared_functions.do_screen_capture_with_filename(driver, "T05S02_3");
		caseNum = shared_functions.convert_case_no_to_cms_format(dict.get("transfer_ward_case_no"));	
		if(cmsMainPage.getPatientIdxByCaseNum(caseNum)==-1) {
			steps_passed = steps_passed + 1	;
			shared_functions.reporter_ReportEvent("micPass","QAG_checkpoint_3", "transfer ward - case no# did not appear in 'transfer from' ward");
		}
		System.out.println("test_transfer_ward() -  verify case no in 'transfer to' ward");
		String wardTransferTo = dict.get("transfer_to_ward");
		cmsMainPage.changeWardForNormalPatientList(wardTransferTo);//7B
		shared_functions.do_screen_capture_with_filename(driver, "T05S02_4");
		caseNum = shared_functions.convert_case_no_to_cms_format(dict.get("transfer_ward_case_no"));
		if(cmsMainPage.getPatientIdxByCaseNum(caseNum) > -1) {
			steps_passed = steps_passed + 1	;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_4", "transfer ward - case no# found in 'transfer to' ward");
		}
		cmsMainPage.fnNextPatient();
		System.out.println("test_transfer_ward() - END");
	}
	
	class PAGE_PatientDetailPage_Transfer {
		WebDriver driver = null;
		public PAGE_PatientDetailPage_Transfer(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe() {
			driver.switchTo().defaultContent();
			driver.switchTo().frame("78Panel");
		}
		public void changeFieldById1(String id, String strNewText) {
			switchToIframe();
			String css = "#ext-comp-"+id+"-inputEl";//ext-comp-{id}-inputEl
			List<WebElement> li = driver.findElements(By.cssSelector( css ));
			WebElement e1 = li.get(0);
			shared_functions.clearAndSend(e1, strNewText);
		}
		public WebElement getFieldById(String id) {
			switchToIframe();
			WebElement e = null;
			String css = "#ext-comp-"+id+"-inputEl";//ext-comp-{id}-inputEl
			List<WebElement> li = driver.findElements(By.cssSelector( css ));
			//if(driver.findElements(By.cssSelector(css)).size()>0) {
				//e = driver.findElements(By.cssSelector(css)).get(0);
			if(li.size()>0) {
				e = li.get(0);
			}
			return e;
		}
		public void clickSaveBtn() {
			switchToIframe();
			String cssSave = "a.x-btn span.x-btn-inner";
			List<WebElement> liSave = driver.findElements(By.cssSelector( cssSave ));
			for(int i=0; i<liSave.size(); i++) {
				if( liSave.get(i).getText().equals("Save") ) {
					liSave.get(i).click();
					break;
				}
			}			
		}
		public void handleWindow() {
			//If transfer_page.WebElement("html tag:=SPAN","class:=x-window-header-text","innertext:=Question / Action \(1-103-1-Q-010\)","visible:=True").Exist(3) Then
			//transfer_page.WebTable("html tag:=TABLE","column names:=;No;","visible:=True").click			
		}
	}

}
