package suite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class T05S07_trial_discharge {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_TrialDischarge trialDischarge = null;
	int steps_passed = 0;
	int total_steps = 2;
	String case_no = null;
	String ward = null;

	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T05S07_trial_discharge";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T05S07");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		trialDischarge = new PAGE_PatientDetailPage_TrialDischarge(driver);
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		case_no = dict.get("trial_discharge_case_no");
		ward = dict.get("ward");
		test_trial_discharge();
		shared_functions.countTestPassed(steps_passed, total_steps);
	}
	public void test_trial_discharge() throws Exception{
		System.out.println("test_trial_discharge() - START HN09000154T LAM,CHERRY");
		cmsMainPage.fnTrialDischarge();
		cmsMainPage.changeWardForNormalPatientList(ward);
		cmsMainPage.selectPatientByCaseNum(case_no);
		psf.closeExistingAlertReminderWindow();
		trialDischarge.getSaveBtn().click();
		shared_functions.do_screen_capture_with_filename(driver, "T05S07_1");
		Boolean isSuccessTrialDischarge = trialDischarge.checkIfTrialDischargeSuccess(); 
		if(isSuccessTrialDischarge) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "trial discharge - the trial discharge successful message is shown");
			steps_passed = steps_passed + 1;
			trialDischarge.getOKBtn().click();
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_1", "trial discharge - the trial discharge successful message is not shown");
		}
		cmsMainPage.fnNextPatient();
		System.out.println("test_trial_discharge() - verify case no# not in PSP after trial discharge");
		cmsMainPage.changeWardForNormalPatientList(ward);
		shared_functions.do_screen_capture_with_filename(driver, "T05S07_2");
		String check_case_no = shared_functions.convert_case_no_to_cms_format(case_no);
		Boolean isExistCaseNo = (cmsMainPage.getPatientIdxByCaseNum(check_case_no) == -1);
		if(isExistCaseNo) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "trial discharge - after trial discharge, case no# not found in PSP");
			steps_passed = steps_passed + 1;			
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_2", "trial discharge - after trial discharge, case no# found in PSP");
		}
		System.out.println("test_trial_discharge() - END");		
	}
	
	class PAGE_PatientDetailPage_TrialDischarge {
		WebDriver driver = null;
		public PAGE_PatientDetailPage_TrialDischarge(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe(){
			driver.switchTo().defaultContent();
			driver.switchTo().frame("1Panel");
		}
		public WebElement getSaveBtn() {
			switchToIframe();
			String str = "Save";
			WebElement e = driver.findElement(By.xpath("//span[contains(text(),'"+str+"')]"));
			return e;			
		}
		public Boolean checkIfTrialDischargeSuccess() {
			switchToIframe();
			String str = "Trial discharge is successful !";
			Boolean b = driver.findElements(By.xpath("//textarea[contains(text(),'"+str+"')]")).size()>0;
			return b;
		}
		public WebElement getOKBtn() {
			switchToIframe();
			String xp = "//span[contains(text(),'K')]//u[contains(text(),'O')]";
			WebElement e = driver.findElement(By.xpath(xp));
			return e;
		}
	}
}
