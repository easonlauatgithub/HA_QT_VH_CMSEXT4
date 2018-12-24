package suite;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import suite.T05S07_trial_discharge.PAGE_PatientDetailPage_TrialDischarge;

public class T05S08_return_from_trial_discharge {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_TrialDischarge trialDischarge = null;
	PAGE_PatientDetailPage_ReturnFromTrialDischarge returnFromTrialDischarge = null;
	int steps_passed = 0;
	int total_steps = 3;
	String case_no = null;
	String ward = null;
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T05S08_return_from_trial_discharge";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T05S08");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		returnFromTrialDischarge = new PAGE_PatientDetailPage_ReturnFromTrialDischarge(driver);
		case_no = dict.get("trial_discharge_case_no");
		ward = dict.get("ward");
		//driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		return_from_trial_discharge_page();
		shared_functions.countTestPassed(steps_passed, total_steps);
	}
	public void return_from_trial_discharge_page() throws Exception{
		System.out.println("return_from_trial_discharge_page - START HN09000154T LAM,CHERRY");
		cmsMainPage.changeWardForNormalPatientList(ward);
		cmsMainPage.fnReturnFromTrialDischarge();
		shared_functions.do_screen_capture_with_filename(driver, "T05S08_1_return_trial_discharge_psp");
		String check_case_no = shared_functions.convert_case_no_to_cms_format(case_no); 
		Boolean isExistCaseNoAfterFnSelected = (cmsMainPage.getPatientIdxByCaseNum(check_case_no)>-1);
		if(isExistCaseNoAfterFnSelected) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "return from trial discharge - case no# found in PSP after function select");
			steps_passed = steps_passed + 1;
		}
		cmsMainPage.selectPatientByCaseNum(case_no);
		psf.closeExistingAlertReminderWindow();
		
		returnFromTrialDischarge.getSaveBtn().click();
		shared_functions.do_screen_capture_with_filename(driver, "T05S08_2");
		Boolean b2 = returnFromTrialDischarge.checkIfReturnFromTrialDischargeSuccess();
		if(b2) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "return from trial discharge - expected [Return from Trial Discharge is successful !] is shown");
			steps_passed = steps_passed + 1;
			returnFromTrialDischarge.getOKBtn().click();
		}
		cmsMainPage.fnNextPatient();
		cmsMainPage.changeWardForNormalPatientList(ward);
		shared_functions.do_screen_capture_with_filename(driver, "T05S08_3");
		Boolean isExistCaseNoBeforeFnSelected = (cmsMainPage.getPatientIdxByCaseNum(check_case_no)>-1);
		if(isExistCaseNoBeforeFnSelected) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "return from trial discharge - case no# found in PSP after return from trial discharge");
			steps_passed = steps_passed + 1;
		}
	}
	
	class PAGE_PatientDetailPage_ReturnFromTrialDischarge {
		WebDriver driver = null;
		public PAGE_PatientDetailPage_ReturnFromTrialDischarge(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe(){
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("80Panel");
		}
		public WebElement getSaveBtn() {
			switchToIframe();
			String str = "Save";
			WebElement e = shared_functions.getElementWhenClickable(By.xpath("//span[contains(text(),'"+str+"')]"));
			return e;			
		}
		public Boolean checkIfReturnFromTrialDischargeSuccess() {
			switchToIframe();
			String str = "Return from Trial Discharge is successful !";
			Boolean b = shared_functions.getElementsWhenVisible(By.xpath("//textarea[contains(text(),'"+str+"')]"))!=null;
			return b;
		}
		public WebElement getOKBtn() {
			switchToIframe();
			String xp = "//span[contains(text(),'K')]//u[contains(text(),'O')]";
			WebElement e = shared_functions.getElementWhenClickable(By.xpath(xp));
			return e;
		}
	}
}
