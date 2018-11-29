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

public class T05S06_modification_of_discharge {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_PMI pmi = null;
	PAGE_PatientDetailPage_ModificationOfDischarge modDischarge = null;
	int steps_passed = 0;
	int total_steps = 1;
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T05S06_modification_of_discharge";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T05S06");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		pmi = new PAGE_PatientDetailPage_PMI(driver);
		modDischarge = new PAGE_PatientDetailPage_ModificationOfDischarge(driver);
		modification_of_discharge();
        cmsMainPage.fnNextPatient();
		shared_functions.countTestPassed(steps_passed, total_steps);
	}
	public void modification_of_discharge() throws Exception {
		System.out.println("modification_of_discharge HN080000251 Wong,Mui");
		String ward = dict.get("ward");
		String case_no = dict.get("discharge_normal_case_no");
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		cmsMainPage.changeWardForNormalPatientList(ward);
		cmsMainPage.fnModificationOfDischarge();
		cmsMainPage.selectPatientByCaseNum(case_no);
		psf.closeExistingAlertReminderWindow();
		System.out.println("modification_of_discharge - select discharge code (2. Home with follow up)");
		WebElement eDischargeCode = modDischarge.getDischargeCodeDrpBx();
		shared_functions.clearAndSend(eDischargeCode, dict.get("modified_discharge_code"));
		WebElement eExistingRemarks = modDischarge.getRemarkTextArea();
		String modified_remark1 = "[NORMAL]: remarks by QTP;";
		String modified_remark2 = " modified at "+ shared_functions.getDateInddMMMyyyy();
		String modified_remarks = modified_remark1 + modified_remark2; //for verify 
		//shared_functions.clearAndSend(eExistingRemarks, modified_remarks);
		eExistingRemarks.clear();
		eExistingRemarks.sendKeys(modified_remark1);
		eExistingRemarks.sendKeys(modified_remark2);
		WebElement eSave = modDischarge.getSaveBtn();
		eSave.click();
		System.out.print("modification_of_discharge - wait for saving, ");
		shared_functions.sleepForAWhile(5000);
		cmsMainPage.fnNextPatient();
		System.out.println("modification_of_discharge - check pmi of the case no#");
		cmsMainPage.fnPMI();
		cmsMainPage.selectPatientByCaseNum(case_no);
		psf.closeExistingAlertReminderWindow();
		shared_functions.do_screen_capture_with_filename(driver, "T05S06_1");
		String check_today_str = shared_functions.getDateInddMMMyyyy();
		String check_case_no = shared_functions.convert_case_no_to_cms_format(case_no);
		String hosp_code = modDischarge.getHospCode();
		List<ArrayList<String>> tableEpisode = pmi.getTableEpisodeCaseList();
        Boolean isCorrectHospCode = tableEpisode.get(0).get(1).equals(hosp_code);
        System.out.println("isCorrectHospCode: "+isCorrectHospCode);
        Boolean isCorrectCaseNo = tableEpisode.get(0).get(3).equals(check_case_no);
        System.out.println("isCorrectCaseNo: "+isCorrectCaseNo);
        Boolean isCorrectDischargeDateTime = tableEpisode.get(0).get(6).substring(0, 11).equals(check_today_str);
        System.out.println("isCorrectDischargeDateTime: "+isCorrectDischargeDateTime);
        Boolean isCorrectDestination = tableEpisode.get(0).get(7).equals("H+FU");
        System.out.println("isCorrectDestination: "+isCorrectDestination);
        Boolean isCorrectWard = tableEpisode.get(0).get(8).equals(dict.get("ward"));
        System.out.println("isCorrectWard: "+isCorrectWard);
        Boolean isCorrectRemark = tableEpisode.get(0).get(12).equals(modified_remarks);
        System.out.println("isCorrectRemarkCMS: "+tableEpisode.get(0).get(12));
        System.out.println("isCorrectRemarkExcel: "+modified_remarks);
        System.out.println("isCorrectRemark: "+isCorrectRemark);
        if(isCorrectHospCode&&isCorrectCaseNo&&isCorrectDischargeDateTime&&isCorrectDestination&&isCorrectWard&&isCorrectRemark) {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "modification of discharge - PMI shows the modified discharge code and remarks");
			steps_passed = steps_passed + 1;
        }
        cmsMainPage.fnNextPatient();
	}
	class PAGE_PatientDetailPage_ModificationOfDischarge {
		WebDriver driver = null;
		public PAGE_PatientDetailPage_ModificationOfDischarge(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframeModDischarge(){
			driver.switchTo().defaultContent();
			driver.switchTo().frame("119Panel");			
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
		public WebElement getDischargeCodeDrpBx() {
			switchToIframeModDischarge();
			String str = "#id_cbx_discharge_code-inputEl";
			WebElement e = driver.findElement(By.cssSelector(str));
			return e;
		}
		public WebElement getRemarkTextArea() {
			switchToIframeModDischarge();
			String str = "#id_txt_remark-inputEl";
			WebElement e = driver.findElement(By.cssSelector(str));
			return e;			
		}
		public WebElement getSaveBtn() {
			switchToIframeModDischarge();
			String str = "Save";
			WebElement e = driver.findElement(By.xpath("//span[contains(text(),'"+str+"')]"));
			return e;			
		}

	}
}
