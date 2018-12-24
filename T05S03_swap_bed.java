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

public class T05S03_swap_bed {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	//PAGE_PatientDetailPage_PMI pmi = null;
	PAGE_PatientDetailPage_SwapBed swapBed = null;
	int steps_passed = 0;
	int total_steps = 3;
	String swap_bed_case_no_A = null;
	String swap_bed_case_no_B = null;
	String bed_A = null;
	String bed_B = null;
	int psp_bed_no_col = 0;

	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T05S03_swap_bed";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T05S03");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		swapBed = new PAGE_PatientDetailPage_SwapBed(driver);
		//driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		swap_bed_case_no_A = dict.get("swap_bed_case_no_A");
		swap_bed_case_no_B = dict.get("swap_bed_case_no_B");
		test_open_function();
		test_swap_bed();
		cmsMainPage.fnNextPatient();
		shared_functions.countTestPassed(steps_passed, total_steps);	
	}
	public void test_open_function() throws Exception {
		System.out.println("test_open_function() - START");
		String ward = dict.get("ward"); //8A
		cmsMainPage.changeWardForNormalPatientList(ward);
		cmsMainPage.fnSwapBed();
		shared_functions.do_screen_capture_with_filename(driver, "T05S03_1");
		driver.switchTo().defaultContent();
		shared_functions.switchToFrameByString("cmsPSPWinPanelCmp");
		Boolean have_bed_no = true;
		for(int i=0; i<cmsMainPage.getNumOfPSPRows(); i++) {
			String bed_no = cmsMainPage.getPSPLeftColumnElement(i).get(psp_bed_no_col).getText();
			if(bed_no.length()==0) {
				have_bed_no = false;
				break;
			}
		}
		if(have_bed_no == true) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "swap bed - before swap, all case no# in PSP are with bed no#");
			steps_passed = steps_passed + 1;
		}
		cmsMainPage.selectPatientByCaseNum(swap_bed_case_no_A);
		psf.closeExistingAlertReminderWindow();
		System.out.println("test_open_function() - END");
	}
	public void test_swap_bed() throws Exception {
		System.out.println("test_swap_bed() - START");
		swapBed.getBtnSelectPatientB().click();
		cmsMainPage.selectPatientByCaseNum(swap_bed_case_no_B);
		psf.closeExistingAlertReminderWindow();
		bed_A = swapBed.getBedNumOfPatientA();
		bed_B = swapBed.getBedNumOfPatientB();
		swapBed.getBtnSwapBed().click();
		shared_functions.do_screen_capture_with_filename(driver, "T05S03_2");
		if(swapBed.isBedSwapped()) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "swap bed - after save, message [Swap Bed is successful !] is found");
			steps_passed = steps_passed + 1;
		}
		swapBed.handleSwapBedPopUpWindow();
		System.out.println("test_swap_bed() - check PSP");
		shared_functions.do_screen_capture_with_filename(driver, "T05S03_3");
		String check_case_no_A = shared_functions.convert_case_no_to_cms_format(swap_bed_case_no_A);
		String check_case_no_B = shared_functions.convert_case_no_to_cms_format(swap_bed_case_no_B);
		Boolean case_A_passed = (cmsMainPage.getBedNumByCaseNum(check_case_no_A).equals(bed_B));
		Boolean case_B_passed = (cmsMainPage.getBedNumByCaseNum(check_case_no_B).equals(bed_A));
		if( case_A_passed && case_B_passed ) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "swap bed - after save, bed no# in psp is swapped.");
			steps_passed = steps_passed + 1;
		}else {
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_3", "swap bed - after save, bed no# in psp is NOT swapped.");
		}
		System.out.println("test_swap_bed() - END");
	}
	
	class PAGE_PatientDetailPage_SwapBed {
		WebDriver driver = null;
		public PAGE_PatientDetailPage_SwapBed(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe() {
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("77Panel");
		}
		public WebElement getBtnSelectPatientB() {
			this.switchToIframe();
			return shared_functions.getElementWhenClickable(By.cssSelector("#btnSelectPatientB-frame1Table"));
		}
		public String getBedNumOfPatientA() {
			this.switchToIframe();
			return shared_functions.getElementWhenClickable(By.cssSelector("#txtBed_no_A-inputEl")).getAttribute("value");
		}
		public String getBedNumOfPatientB() {
			this.switchToIframe();
			return shared_functions.getElementWhenClickable(By.cssSelector("#txtBed_no_B-inputEl")).getAttribute("value");
		}
		public WebElement getBtnSwapBed() {
			this.switchToIframe();
			return shared_functions.getElementWhenClickable(By.cssSelector("#btnSwapBed-frame1Table"));
		}
		public Boolean isBedSwapped() {
			this.switchToIframe();
			return shared_functions.checkAndGetElementsWhenVisible(By.xpath("//textarea[contains(text(),'Swap Bed is successful !')]"))!=null;
		}
		public void handleSwapBedPopUpWindow() {
			List<WebElement> li = shared_functions.checkAndGetElementsWhenVisible(By.xpath("//span[contains(text(),'K')]//u[contains(text(),'O')]"));
			if(li!=null) {
				li.get(0).click();
			}
		}

	}

	
}
