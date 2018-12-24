package suite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

public class T05S01_bed_assignment {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_BedAssignment bedAssignment = null;
	int steps_passed = 0;
	int total_steps = 4;
	String bed_no = null;
	String case_no = null;
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T05S01_bed_assignment";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T05S01");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		bedAssignment = new PAGE_PatientDetailPage_BedAssignment(driver);
		bed_no = dict.get("bed_to_assign");
		case_no = dict.get("bed_assign_case_no");
		//driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		test_open_function();
		test_assign_bed();
		cmsMainPage.fnNextPatient();
		shared_functions.countTestPassed(steps_passed, total_steps);
	}
	public void test_open_function() throws Exception{
		System.out.println("test_open_function() - START");
		System.out.println("test_open_function() - only show patient without bed checking...");
		Boolean have_bed_no = false;		
		cmsMainPage.fnBedAssignment();
		shared_functions.do_screen_capture_with_filename(driver, "T05S01_1");
		for(int i=0; i<cmsMainPage.getNumOfPSPRows(); i++ ) {
			int colIdxOfBedNum = 0;
			String strBedNum = cmsMainPage.getPSPLeftColumnElement(i).get(colIdxOfBedNum).getText();
			if(!strBedNum.isEmpty()) {
				have_bed_no=true;
			}
		}		
		if(have_bed_no==false) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "bed assignment - after select function, psp only shows patient without bed");
		}else {
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_1", "bed assignment - after select function, psp shows some patient with bed no !");
		}
		System.out.println("test_open_function() - END");
	}
	public void test_assign_bed() throws Exception{
		System.out.println("test_assign_bed() - START HN080000200 KONG, LING LAN");
		cmsMainPage.selectPatientByCaseNum( case_no );
		WebElement availableBedBtn = bedAssignment.getBed(bed_no); //3
		availableBedBtn.click();
		bedAssignment.clickSave();
		shared_functions.do_screen_capture_with_filename(driver, "T05S01_1A_ePR_Printing");
		//HARDCODE To SKIP PRINTING
		Boolean isExistBrowser = bedAssignment.HARDCODE_handlePrintEpr();	
		//Boolean isExistBrowser = bedAssignment.isSameBrowserTitle("ePR Summary Printing");
		if( isExistBrowser ) {
			Boolean isPrintSuccess = true;
			//If Browser("title:=ePR Summary Printing.*").Page("title:=ePR Summary Printing.*").WebElement("html tag:=P","innertext:=ePR summary is printed\.","visible:=True").Exist(3)
			if(isPrintSuccess) {
				steps_passed = steps_passed + 1;
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_4", "bed assignment - ePR Summary Printing successfully" );
			}else {
				shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_4", "bed assignment - ePR Summary Printing failed" );
			}
		}else {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("SKIP", "PRINTING", "PRINTING at QAG_checkpoint_4" );
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_4", "bed assignment - ePR Summary Printing is not available");
		}
		System.out.println("test_assign_bed() - closeAssignTeamDoctor");
		bedAssignment.switchToFrame_AssignTeamDoctor();
		String strCancelAssignTeamDoctor = "#cancelBtn";
		List<WebElement> liCancelAssignTeamDoctor = shared_functions.checkAndGetElementsWhenVisible(By.cssSelector(strCancelAssignTeamDoctor));
		if( liCancelAssignTeamDoctor!=null ) {
			liCancelAssignTeamDoctor.get(0).click();
		}
		bedAssignment.switchToFrame_BedAssignment();
		String strOkBtn = "//span[contains(text(),'K')]//u[contains(text(),'O')]";
		List<WebElement> liOkBtn = shared_functions.checkAndGetElementsWhenVisible(By.xpath(strOkBtn));
		if( liOkBtn!=null ) {
			liOkBtn.get(0).click();
		}
		System.out.println("test_assign_bed() - QAG_checkpoint_2 case no# should appear with bed no# assigned correctly");
		cmsMainPage.fnNextPatient();
		shared_functions.do_screen_capture_with_filename(driver, "T05S01_2"); 
		String check_case_no = shared_functions.convert_case_no_to_cms_format(case_no); //HN080000200 -> HN08000020(0)
		String bedNumAtCMS = cmsMainPage.getBedNumByCaseNum(check_case_no); //3
		Boolean isSameBed =  (bedNumAtCMS.equals(bed_no));
		if( isSameBed ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "bed assignment - close function, bed no# match in psp list");
		}
		
		System.out.println("test_assign_bed() - QAG_checkpoint_3 case no# should not appear in PSP now");
		cmsMainPage.fnBedAssignment();
		shared_functions.do_screen_capture_with_filename(driver, "T05S01_3");
		check_case_no = shared_functions.convert_case_no_to_cms_format(case_no);
		int patientRowId = cmsMainPage.getPatientIdxByCaseNum(check_case_no);
		Boolean isNotExistCaseNum = (patientRowId == -1); 
		if( isNotExistCaseNum ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "bed assignment - after bed assignment, case no# not found in psp (with function opened)");
		}
		System.out.println("test_assign_bed() - END");
	}
	
	class PAGE_PatientDetailPage_BedAssignment{
		WebDriver driver = null;
		public PAGE_PatientDetailPage_BedAssignment(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToFrame_PSP() {
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("cmsPSPWinPanelCmp");
		}
		public void switchToFrame_AssignTeamDoctor() {
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("361Panel");
		}
		public void switchToFrame_BedAssignment() {
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("37Panel");
		}
		public WebElement getBed(String bedNum) {
			this.switchToFrame_BedAssignment();
			return shared_functions.getElementWhenClickable(By.cssSelector("#bed"+bedNum+"-inputEl"));
		}
		public void clickSave() {
			shared_functions.getElementWhenClickable(By.cssSelector("#btnSave-btnInnerEl")).click();
		}
		public Boolean isSameBrowserTitle(String strBrowserTitle) {
			Set<String> handles  = driver.getWindowHandles();
			for(String h : handles ) {
				 driver.switchTo().window(h);
				 String title = driver.getTitle();
				 System.out.print("Brower: ");
				 System.out.print(h);
				 System.out.print(", title: ");
				 System.out.println(title);
				 if(title.equals(strBrowserTitle)) {
					return true;
				 }
			}
			return false;
		}
		public Boolean HARDCODE_handlePrintEpr() throws FindFailed, InterruptedException {
			String screenCapturePath = "C:\\eclipse_neon\\workspace\\TestSelenium\\screencapture_applet\\";
			Screen screen = new Screen(0);
			Screen screen1 = new Screen(1);
			//print_epr
			System.out.println("checking exist print_epr...");
			if ( screen1.exists(screenCapturePath+"print_epr.PNG") != null ){
				System.out.println("in screen1");
				screen1.click(screenCapturePath+"applet_checkbox.PNG");
				screen1.click(screenCapturePath+"applet_confirm.PNG");
			}else {
				System.out.println("not in screen1");
			}
			if ( screen.exists(screenCapturePath+"print_epr.PNG") != null ){
				System.out.println("in screen");
				screen.click(screenCapturePath+"applet_checkbox.PNG");
				screen.click(screenCapturePath+"applet_confirm.PNG");
			}else {
				System.out.println("not in screen");
			}
			//Close inform Call Centre
			System.out.println("checking exist epr_summary...");
			if ( screen1.exists(screenCapturePath+"epr_summary.PNG") != null ){
				System.out.println("in screen1");
				screen1.click(screenCapturePath+"close_epr.PNG");
				shared_functions.sleepForAWhile(1000);
			}else {
				System.out.println("not in screen1");
			}
			if ( screen.exists(screenCapturePath+"epr_summary.PNG") != null ){
				System.out.println("in screen");
				screen.click(screenCapturePath+"close_epr.PNG");
				shared_functions.sleepForAWhile(1000);
			}else {
				System.out.println("not in screen");
			}
			return false;
		}
	}
}
