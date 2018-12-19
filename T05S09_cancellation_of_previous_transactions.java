package suite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class T05S09_cancellation_of_previous_transactions {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_CancelPreviousTransaction cpt = null;
	int steps_passed = 0;
	int total_steps = 0;
	String check_date = null;
	@Test
	public void main() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T05S09_cancellation_of_previous_transactions";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T05S09ex");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		cpt = new PAGE_PatientDetailPage_CancelPreviousTransaction(driver);
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		//decide run or not
		Boolean b_bad_assignment_enable = dict.get("bad_assignment_enable").equals("Y");
		Boolean b_transfer_enable = dict.get("transfer_enable").equals("Y");
		Boolean b_swap_bed_enable = dict.get("swap_bed_enable").equals("Y");
		Boolean b_trial_discharge_enable = dict.get("trial_discharge_enable").equals("Y");
		Boolean b_discharge_enable = dict.get("discharge_enable").equals("Y");
		System.out.println("b_bad_assignment_enable: "+b_bad_assignment_enable);
		System.out.println("b_transfer_enable: "+b_transfer_enable);
		System.out.println("b_swap_bed_enable: "+b_swap_bed_enable);
		System.out.println("b_trial_discharge_enable: "+b_trial_discharge_enable);
		System.out.println("b_discharge_enable: "+b_discharge_enable);
		//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  HARDCODE START XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX 
		b_bad_assignment_enable = true;
		b_transfer_enable = true;
		b_swap_bed_enable = true;
		b_discharge_enable = true;
		b_trial_discharge_enable = true;
		//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  HARDCODE END XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		//get check_date
		if(dict.get("tx_date_to_cancel").equals("today") ) {
			check_date = shared_functions.getDateIn("dd-MMM-yyyy");
		}else if(dict.get("tx_date_to_cancel").equals("this_month") ) {
			check_date = shared_functions.getDateIn("MMM-yyyy");
		}
		System.out.println("check_date: "+check_date);
		if( check_date.equals("") ) {
			shared_functions.reporter_ReportEvent("micFail", "QAG_failure", "no tx date defined to cancel, please check datasheet");
		}
		dict.put("previous_ward", "");
		dict.put("previous_specialty", "");
		dict.put("previous_bed_no", "");
		dict.put("previous_category", "");
		dict.put("current_ward", "");		
		//run
		if(b_bad_assignment_enable){
			total_steps = total_steps + 1;
			cancel_bed_assignment();
		}
		if(b_transfer_enable){
			total_steps = total_steps + 4;
			cancel_transfer();
		}
		if(b_swap_bed_enable){
			total_steps = total_steps + 1;
			cancel_swap_bed();
		}
		if(b_trial_discharge_enable){
			total_steps = total_steps + 2;
			cancel_trial_discharge();
		}
		if(b_discharge_enable){
			total_steps = total_steps + 2;
			cancel_discharge();
		}
		cmsMainPage.fnNextPatient();
		shared_functions.countTestPassed(steps_passed, total_steps);
	}
	
	public void cancel_bed_assignment() throws Exception {
		System.out.println("cancel_bed_assignment() - HN080000200 KONG, LING LAN");
		String case_no = dict.get("bed_assign_case_no"); //HN080000200
		String ward = dict.get("bed_assign_ward");//8A
		cmsMainPage.fnCancelPreviousTransaction();
		cmsMainPage.changeWardForNormalPatientList( ward ); 
		cmsMainPage.selectPatientByCaseNum(case_no);
		psf.closeExistingAlertReminderWindow();
		Boolean retval = cpt.cancelPreviousTransaction("Bed Assignment");
		cmsMainPage.fnNextPatient();
		System.out.println("cancel_bed_assignment() - verift case no is exist and without bed num");
		shared_functions.do_screen_capture_with_filename(driver, "T05S09_1");
		if(retval) {
			String check_case_no = shared_functions.convert_case_no_to_cms_format(case_no);
			Boolean b = cmsMainPage.getBedNumByCaseNum(check_case_no).equals("");
			if(b) {
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "cancel bed assignment - case no# is with empty bed no# in PSP");
				steps_passed = steps_passed + 1;
			}
		}else {
			shared_functions.reporter_ReportEvent("micDone", "cancel bed assignment", "no checking is made");
		}
	}
	public void cancel_transfer() throws Exception {
		String case_no = null;
		String ward = null;
		Boolean retval = null;
		System.out.println("cancel_transfer() - HN080000642 LI,HOI TONG ");
		case_no = dict.get("transfer_category_case_no");
		ward = dict.get("transfer_from_ward");
		cmsMainPage.changeWardForNormalPatientList(ward);
		cmsMainPage.fnCancelPreviousTransaction();
		cmsMainPage.selectPatientByCaseNum(case_no);
		psf.closeExistingAlertReminderWindow();
		retval = cpt.cancelPreviousTransaction("Transfer");
		cmsMainPage.fnNextPatient();
		System.out.println("cancel_transfer() - verify category reverted  Class 2->3");
		shared_functions.do_screen_capture_with_filename(driver, "T05S09_2");
		if(retval) {
			String check_case_no = shared_functions.convert_case_no_to_cms_format(case_no);
			Boolean b = cmsMainPage.getCategoryByCaseNum(check_case_no).equals(dict.get("previous_category"));
			if(b) {
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "cancel transfer category - category reverted in PSP");
				steps_passed = steps_passed + 1;
			}
		}else {
			shared_functions.reporter_ReportEvent("micDone", "cancel transfer category", "no checking is made");
		}

		System.out.println("cancel_transfer() HN08000026Z LEUNG,APPLE");
		case_no = dict.get("transfer_specialty_case_no");
		cmsMainPage.fnCancelPreviousTransaction();
		cmsMainPage.selectPatientByCaseNum(case_no);
		psf.closeExistingAlertReminderWindow();
		retval = cpt.cancelPreviousTransaction("Transfer"); 
		cmsMainPage.fnNextPatient();
		System.out.println("cancel_transfer() - verify specialty reverted SUR->MED");
		shared_functions.do_screen_capture_with_filename(driver, "T05S09_3");
		if(retval) {
			String check_case_no = shared_functions.convert_case_no_to_cms_format(case_no);
			String strSpecialtyInCms = cmsMainPage.getSpecialtyByCaseNum(check_case_no).trim();
			String strSpecialtyInExcel = dict.get("previous_specialty");
			Boolean b = strSpecialtyInCms.equals(strSpecialtyInExcel);
			System.out.println("strSpecialtyInCms: "+strSpecialtyInCms);
			System.out.println("strSpecialtyInExcel: "+strSpecialtyInExcel);
			System.out.println("Same?: "+b);
			if(b) {
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "cancel transfer specialty - specialty reverted in PSP");
				steps_passed = steps_passed + 1;
			}
		}else {
			shared_functions.reporter_ReportEvent("micDone", "cancel transfer specialty", "no checking is made");
		}	

		System.out.println("cancel_transfer() 8A->7B HN08000013X KONG,WING JAK");
		case_no = dict.get("transfer_ward_case_no");
		ward = dict.get("transfer_from_ward");
		cmsMainPage.changeWardForNormalPatientList(ward);
		cmsMainPage.fnCancelPreviousTransaction();
		cmsMainPage.selectPatientByCaseNum(case_no);
		psf.closeExistingAlertReminderWindow();
		retval = cpt.cancelPreviousTransaction("Transfer"); 
		cmsMainPage.fnNextPatient();
		if(retval) {
			String check_ward = null;
			Boolean b = null;
			String check_case_no = shared_functions.convert_case_no_to_cms_format(case_no);
			System.out.println("cancel_transfer() - verify no case no in current ward "+dict.get("current_ward"));
			check_ward = dict.get("current_ward");
			cmsMainPage.changeWardForNormalPatientList(check_ward);
			shared_functions.do_screen_capture_with_filename(driver, "T05S09_4");
			b = (cmsMainPage.getPatientIdxByCaseNum(check_case_no)==-1);
			if(b) {
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_4", "cancel transfer ward - case no# NOT found in 'before cancel transfer' ward");
				steps_passed = steps_passed + 1;
			}
			System.out.println("cancel_transfer() - verify case no in previous ward "+dict.get("previous_ward"));
			check_ward = dict.get("previous_ward");
			cmsMainPage.changeWardForNormalPatientList(check_ward);
			shared_functions.do_screen_capture_with_filename(driver, "T05S09_5");
			b = (cmsMainPage.getPatientIdxByCaseNum(check_case_no)>-1);
			if(b) {
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_5", "cancel transfer ward - case no# found in 'previous ward'");
				steps_passed = steps_passed + 1;
			}

		}else {
			shared_functions.reporter_ReportEvent("micDone", "cancel transfer specialty", "no checking is made");
		}
	}
	public void cancel_swap_bed() throws Exception {
		System.out.println("Cancel Swap Bed - HN08000029T/CHAN,PEARS HN05000252S/YUEN, SHUI SIN");
		Boolean bRetval = false;
		cmsMainPage.fnCancelPreviousTransaction();
		String case_no = dict.get("swap_bed_case_no_A");
		cmsMainPage.selectPatientByCaseNum( case_no );
		psf.closeExistingAlertReminderWindow();
		bRetval = cpt.cancelPreviousTransaction("Bed Assignment");
		
		case_no = dict.get("swap_bed_case_no_B");
		cmsMainPage.selectPatientByCaseNum( case_no );
		psf.closeExistingAlertReminderWindow();
		bRetval = cpt.cancelPreviousTransaction("Bed Assignment");
		
		case_no = dict.get("swap_bed_case_no_A");
		cmsMainPage.selectPatientByCaseNum( case_no );
		psf.closeExistingAlertReminderWindow();
		bRetval = cpt.cancelPreviousTransaction("Transfer");
		
		String previous_bed_A = dict.get("previous_bed_no");
		
		case_no = dict.get("swap_bed_case_no_B");
		cmsMainPage.selectPatientByCaseNum( case_no );
		psf.closeExistingAlertReminderWindow();
		bRetval = cpt.cancelPreviousTransaction("Transfer");
		cmsMainPage.fnNextPatient();
		
		String previous_bed_B = dict.get("previous_bed_no");
		
		if(bRetval){
			shared_functions.do_screen_capture_with_filename(driver, "T05S09_6");
			String check_case_no_A = shared_functions.convert_case_no_to_cms_format(dict.get("swap_bed_case_no_A"));
			String check_case_no_B = shared_functions.convert_case_no_to_cms_format(dict.get("swap_bed_case_no_B"));
			String check_case_no_A_bed_no = cmsMainPage.getBedNumByCaseNum(check_case_no_A);
			String check_case_no_B_bed_no = cmsMainPage.getBedNumByCaseNum(check_case_no_B);
			Boolean case_A_pass = false;
			Boolean case_B_pass = false;
			System.out.println("previous_bed_A:"+previous_bed_A);
			System.out.println("previous_bed_B:"+previous_bed_B);
			System.out.println("check_case_no_A_bed_no:"+check_case_no_A_bed_no);
			System.out.println("check_case_no_B_bed_no:"+check_case_no_B_bed_no);
			
			if(check_case_no_A_bed_no.equals(previous_bed_A)){
				case_A_pass = true;
			}
			if(check_case_no_B_bed_no.equals(previous_bed_B)){
				case_B_pass = true;
			}
			System.out.println("cancel_swap_bed() - verify the bed no# of 2 patients reverted");
			if(case_A_pass&&case_B_pass){
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_6", "cancel swap bed - the bed no# of 2 patients reverted");
				steps_passed+=1;
			}
		}else{
			shared_functions.reporter_ReportEvent("micDone", "cancel swap bed", "no checking is made");
		}
	}
	public void cancel_trial_discharge() throws Exception {
			System.out.println("Cancel Return Trial Discharge - HN09000154T LAM,CHERRY");
			cmsMainPage.changeWardForNormalPatientList(dict.get("trial_discharge_ward"));
			String case_no = dict.get("trial_discharge_case_no");
			cmsMainPage.selectPatientByCaseNum( case_no );
			cmsMainPage.fnCancelPreviousTransaction();
			psf.closeExistingAlertReminderWindow();
			Boolean bRetval = cpt.cancelPreviousTransaction("Trial Discharge Ret.");
			cmsMainPage.fnNextPatient();
			shared_functions.do_screen_capture_with_filename(driver, "T05S09_7");
			if(bRetval){
				cmsMainPage.changeWardForNormalPatientList(dict.get("trial_discharge_ward"));
				String check_case_no = shared_functions.convert_case_no_to_cms_format(dict.get("trial_discharge_case_no"));
				Boolean isNotExistPatient = (cmsMainPage.getPatientIdxByCaseNum(check_case_no)==-1);
				if(isNotExistPatient){
					shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_7", "cancel return from trial discharge - case no# is NOT found in PSP");
					steps_passed = steps_passed + 1;
				}
			}else{
				shared_functions.reporter_ReportEvent("micDone", "cancel return from trial discharge", "no checking is made");
			}
			cmsMainPage.changeWardForNormalPatientList(dict.get("trial_discharge_ward"));
			case_no = dict.get("trial_discharge_case_no");
			cmsMainPage.selectPatientByCaseNum( case_no );
			cmsMainPage.fnCancelPreviousTransaction();
			psf.closeExistingAlertReminderWindow();
			bRetval = cpt.cancelPreviousTransaction("Trial Discharge");
			cmsMainPage.fnNextPatient();
			shared_functions.do_screen_capture_with_filename(driver, "T05S09_8");
			if(bRetval){
				cmsMainPage.changeWardForNormalPatientList(dict.get("trial_discharge_ward"));
				String check_case_no = shared_functions.convert_case_no_to_cms_format(dict.get("trial_discharge_case_no"));
				Boolean isNotExistPatient = (cmsMainPage.getPatientIdxByCaseNum(check_case_no)>=0);
				if(isNotExistPatient){
					shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_8", "cancel trial discharge - case no# found in PSP");
					steps_passed = steps_passed + 1;
				}
			}else{
				shared_functions.reporter_ReportEvent("micDone", "cancel trial trial discharge", "no checking is made");
			}
	}
	public void cancel_discharge() throws Exception {
		String case_no = null;
		String ward = dict.get("discharge_ward");
		Boolean retval = false;
	
		System.out.println("cancel_discharge() - HN080000251 WONG,MUI");
		cmsMainPage.fnCancelPreviousTransaction();
		cmsMainPage.changeWardForNormalPatientList(ward);
		case_no = dict.get("discharge_normal_case_no");
		cmsMainPage.selectPatientByCaseNum( case_no );
		psf.closeExistingAlertReminderWindow();
		retval = cpt.cancelPreviousTransaction("Discharge");
		cmsMainPage.fnNextPatient();
		System.out.println("cancel_discharge() - verift case no HN080000251 WONG,MUI is exist");
		shared_functions.do_screen_capture_with_filename(driver, "T05S09_9");
		if(retval) {
			cmsMainPage.changeWardForNormalPatientList(ward);
			String check_case_no = shared_functions.convert_case_no_to_cms_format(case_no);
			Boolean b = cmsMainPage.getPatientIdxByCaseNum(check_case_no)!=-1;
			if(b) {
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_9", "cancel discharge normal - case no# is found in PSP");
				steps_passed = steps_passed + 1;
			}
		}else {
			shared_functions.reporter_ReportEvent("micDone", "cancel discharge normal", "no checking is made");
		}
		
		System.out.println("cancel_discharge() - HN08000032T WONG,PAPAYA");
		cmsMainPage.fnCancelPreviousTransaction();
		cmsMainPage.changeWardForNormalPatientList(ward);
		case_no = dict.get("discharge_death_case_no");
		cmsMainPage.selectPatientByCaseNum( case_no );
		psf.closeDeadWarning();
		psf.closeExistingAlertReminderWindow();
		retval = cpt.cancelPreviousTransaction("Discharge");
		cmsMainPage.fnNextPatient();
		System.out.println("cancel_discharge() - verift case no HN08000032T WONG,PAPAYA is exist");
		shared_functions.do_screen_capture_with_filename(driver, "T05S09_10");
		if(retval) {
			cmsMainPage.changeWardForNormalPatientList(ward);
			String check_case_no = shared_functions.convert_case_no_to_cms_format(case_no);
			Boolean b = cmsMainPage.getPatientIdxByCaseNum(check_case_no)!=-1;
			if(b) {
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_10", "cancel discharge death - case no# is found in PSP");
				steps_passed = steps_passed + 1;
			}
		}else {
			shared_functions.reporter_ReportEvent("micDone", "cancel discharge normal", "no checking is made");
		}
		
	}
	
	class PAGE_PatientDetailPage_CancelPreviousTransaction {
		WebDriver driver = null;
		public PAGE_PatientDetailPage_CancelPreviousTransaction(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe(){
			driver.switchTo().defaultContent();
			driver.switchTo().frame("82Panel");			
		}
		public Boolean cancelPreviousTransaction(String tx_type_to_cancel) {
			switchToIframe();
			Boolean do_cancel = false;
			String xp = "//input[ @id='CancelDate-inputEl' and @value='"+check_date+"' ]";
			Boolean isExistCancelDate = driver.findElements(By.xpath(xp)).size()>0; 
			if(isExistCancelDate) {
				do_cancel = true;
			}else {
				shared_functions.reporter_ReportEvent("micWarning", "Cancel "+tx_type_to_cancel, "no today("+ check_date +")'s tx to cancel");
			}
			Boolean retval = false;
			if(do_cancel) {
				dict.put("previous_ward", getDataPreviousWard());
				dict.put("previous_specialty", getDataPrevSpecialty());
				dict.put("previous_bed_no", getPrevBedNo());
				dict.put("previous_category", getPrevClass());
				dict.put("current_ward", getCancelWardCode());				
				clickYesBtnInBottomRight();
				String textarea_text_check = "Cancel " + tx_type_to_cancel;
				if(tx_type_to_cancel.equals("Trial Discharge Ret.")) {
					textarea_text_check = "Cancel Return From Trial Discharge";
				}
				Boolean isExistWordings = driver.findElements(By.xpath("//textarea[contains(text(),'"+textarea_text_check+"')]")).size()>0;
				if(!isExistWordings) {
					shared_functions.reporter_ReportEvent("micFail", "cancel previous transaction confirmation error", "expected text [Cancel " + tx_type_to_cancel + "] does not appear");
				}else {
					clickYesBtnInPopUpWindow();
					retval = true;
				}
			}else {
				clickNoBtnInBottomRight();
			}
			return retval;
		}
		public String getDataPreviousWard() {
			String value = null;
			String css = "#PrevWardCode-inputEl";
			if( driver.findElements(By.cssSelector(css)).size()>0 ) {
				value = driver.findElements(By.cssSelector(css)).get(0).getAttribute("value");
			}			
			return value;
		}
		public String getDataPrevSpecialty() {
			String value = null;
			String css = "#PrevSpecialty-inputEl";
			if( driver.findElements(By.cssSelector(css)).size()>0 ) {
				value = driver.findElements(By.cssSelector(css)).get(0).getAttribute("value");
			}
			return value;
		}
		public String getPrevBedNo() {
			String value = null;
			String css = "#PrevBedNo-inputEl";
			if( driver.findElements(By.cssSelector(css)).size()>0 ) {
				value = driver.findElements(By.cssSelector(css)).get(0).getAttribute("value");
			}			
			return value;
		}
		public String getPrevClass() {
			String value = null;
			String css = "#PrevClass-inputEl";
			if( driver.findElements(By.cssSelector(css)).size()>0 ) {
				value = driver.findElements(By.cssSelector(css)).get(0).getAttribute("value");
			}			
			return value;
		}
		public String getCancelWardCode() {
			String value = null;
			String css = "#CancelWardCode-inputEl";
			if( driver.findElements(By.cssSelector(css)).size()>0 ) {
				value = driver.findElements(By.cssSelector(css)).get(0).getAttribute("value");
			}
			return value;
		}

		public void clickYesBtnInBottomRight() {
			String css = "#copt-yesbutton-frame1Table";
			if( driver.findElements(By.cssSelector(css)).size()>0 ) {
				driver.findElements(By.cssSelector(css)).get(0).click();
			}
		}
		public void clickNoBtnInBottomRight() {
			String css = "#copt-nobutton-frame1Table";
			if( driver.findElements(By.cssSelector(css)).size()>0 ) {
				driver.findElements(By.cssSelector(css)).get(0).click();
			}
		}
		public void clickYesBtnInPopUpWindow() {
			String xp = "//span[contains(text(),'es')]//u[contains(text(),'Y')]";
			if( driver.findElements(By.xpath(xp)).size()>0 ) {
				driver.findElements(By.xpath(xp)).get(0).click();
			}
		}

	}	
}
