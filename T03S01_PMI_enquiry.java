package suite;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class T03S01_PMI_enquiry {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_PMI pmi = null;
	int steps_passed = 0;
	int total_steps = 3;
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T03S01_PMI_enquiry";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T03S01_extjs4");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		pmi = new PAGE_PatientDetailPage_PMI(driver);
		//driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		open_function();
		check_function();		
		cmsMainPage.fnNextPatient();
		shared_functions.countTestPassed(steps_passed, total_steps);
		//driver.quit();
	}
	public void open_function() throws InterruptedException{
		System.out.println("open_function() - START");
		cmsMainPage.fnPMI();
		cmsMainPage.selectPatientByCaseNum( dict.get("case_no") ); //PMI(WONG, GUAVA)
		psf.closeExistingAlertReminderWindow();
		System.out.println("open_function() - END");
	}
	public void check_function() throws Exception{
		System.out.println("check_function() - START");
		System.out.println("check_function() - Verify Patient Adderss T2315");
		pmi.clickTab_PMIEnq();
		pmi.expandPatientAddress();
		shared_functions.do_screen_capture_with_filename(driver, "T03S01_1");
		String strBuilding = pmi.getPatientAddress();
		Boolean isCorrectBuilding = strBuilding.equals( dict.get("check_address") );
		if( isCorrectBuilding ) {
			steps_passed = steps_passed + 1	;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "address building match");
		}
		pmi.collapsePatientAddress();
		System.out.println("check_function() - Verify Patient Demographic T2400");
		pmi.expandPatientDemographic();
		Map<String,WebElement> mapItemsToVerify = pmi.getMapOfPatientDemographicData();
		Set<String> setOfKeys = mapItemsToVerify.keySet();
		int counterSame = 0;
		for(String k : setOfKeys ) {
			String strToVerify = mapItemsToVerify.get(k).getAttribute("value").toString();
			String strInExcel = dict.get( "check_"+k ).toString();
			if( strToVerify.equals(strInExcel) ) {
				counterSame ++;
			}
		}
		shared_functions.do_screen_capture_with_filename(driver, "T03S01_2");
		Boolean isSameAllItem = (counterSame == mapItemsToVerify.size());
		if( isSameAllItem ) {
			steps_passed = steps_passed + 1	;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "patient demographics match - HKID, chi name, eng name, case no#, dob, ageo, sex");
		}

		System.out.println("PAGE - PMI(WONG, GUAVA) - Verify NOK T2401");
		pmi.clickTab_NOK();
		shared_functions.sleepForAWhile(1000);
		String strNokNameToVerify = pmi.getNokName();
		String strMajorNokToVerify = pmi.getMajorNok();
		shared_functions.do_screen_capture_with_filename(driver, "T03S01_3");
		Boolean isSameNokName = strNokNameToVerify.equals( dict.get("check_nok_name") ); 
		Boolean isSameMajorNok = strMajorNokToVerify.equals( dict.get("check_major_nok") ); 
		if( isSameNokName && isSameMajorNok ) {
			steps_passed = steps_passed + 1	;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "patient nok match - name, major_nok");
		}
		System.out.println("check_function() - END");
	}
}
