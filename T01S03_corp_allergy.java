package suite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class T01S03_corp_allergy {
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_AlertPanel alertPanel = null;
	PAGE_AlertPanel_Allergen apAllergen= null;
	PAGE_AlertPanel_ADR apADR= null;
	PAGE_AlertPanel_Alert apAlert= null;
	int steps_passed ;
	int total_steps ; 
	Map<String, String> dict = new HashMap<>();
	String moe_case_no = null;
	
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T01S03_corp_allergy";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T01S03_extjs4");
		dict = shared_functions.sam_gor();
		
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		alertPanel = null;
		apAllergen = new PAGE_AlertPanel_Allergen(driver);
		apADR = new PAGE_AlertPanel_ADR(driver);
		apAlert = new PAGE_AlertPanel_Alert(driver);
		steps_passed = 0;
		total_steps = 23;
		moe_case_no = dict.get("case_no");
		//driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		
		test_enquiry_previous_record();
		test_nkda();
		test_allergy();
		test_nondrug_allergy();
		test_advdrug_allergy();
		test_non_classified_alert();
		test_structured_classified_alert();
		test_freetext_classified_alert();
		shared_functions.countTestPassed(steps_passed, total_steps);		
		cmsMainPage.fnNextPatient();
	}
	//Module
	public void test_enquiry_previous_record() throws Exception {
		System.out.println("test_enquiry_previous_record() - START");		
		cmsMainPage.fnPMI();
		cmsMainPage.selectPatientByCaseNum( dict.get("case_no_with_allergy").toString() ); //HN08000013X, Kong Wing Jak
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_1");
		System.out.println("QAG_checkpoint_1 test_enquiry_previous_record() - verify ASPIRIN alert");
		Boolean isExistAlert = false;
		String strForVerify = dict.get("existing_adv_drug_check").toString();
		driver.switchTo().defaultContent();
		shared_functions.switchToFrameByString("alertWinPanelModalIFrame");
		List<WebElement> liAlertItem = null;
		Boolean bToGetAllAlertItem = true;
		while(bToGetAllAlertItem){
			try{				
				liAlertItem = shared_functions.getElementsWhenVisible(By.cssSelector("div#divAllergyId table table td:nth-child(2)"));
				bToGetAllAlertItem=false;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Iterator<WebElement> itrAlertItem = liAlertItem.iterator();
		while( itrAlertItem.hasNext() ) {
			String strAlertItem = itrAlertItem.next().getText();
			if( strForVerify.equals( strAlertItem ) ) {
				isExistAlert =true;
			}
		}
		if( isExistAlert ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "enquiry previous record - simplified allergy auto-prompted");
			alertPanel = psf.clickDetailBtnOfExistingAlertReminderWindow();
			shared_functions.do_screen_capture_with_filename(driver, "T01S03_1A_printAllergy");
			shared_functions.reporter_ReportEvent("SKIP", "PRINT", "click print btn on Alert btn"); //print allergy
		}else {
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_1", "enquiry previous record - simplified allergy NOT found by auto-prompt");			
		}
		alertPanel.closeDetailAlertPanel();
		System.out.println("QAG_checkpoint_2 test_enquiry_previous_record() - verify Alert button color is red");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_2");
		if( psf.checkAlertBtn("red" ) ) { 
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "enquiry previous record - red spio alert button is found");
		}else {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "enquiry previous record - red spio alert button is NOT found");
		}
		cmsMainPage.fnNextPatient();
		System.out.println("test_enquiry_previous_record() - END");
	}
	public void test_nkda() throws Exception {	
		System.out.println("test_nkda() - START");
		System.out.println("test_nkda() - PMI, HN08000026Z, LEUNG APPLE - remove NKDA if Alert button color is red");
		cmsMainPage.fnPMI();
		cmsMainPage.selectPatientByCaseNum( moe_case_no ); //HN08000026Z, LEUNG APPLE
		if( psf.checkAlertBtn("red") ) {
			shared_functions.reporter_ReportEvent("micFail", "QAG_test_initialization", "test nkda - the case no# should not have previous allergy record");
		}else {
			alertPanel = psf.clickAlertBtn();
			WebElement chkBx_NKDA = alertPanel.getChkBoxNKDA();
			if( chkBx_NKDA.getAttribute("value").equals("true") ) {
				chkBx_NKDA.click();
				shared_functions.reporter_ReportEvent("micDone", "removed existing NKDA option", "");
			}
		}
		alertPanel.closeAlertPanel();
		cmsMainPage.fnNextPatient();
		System.out.println("QAG_checkpoint_3 test_nkda() - PMI, HN08000026Z, LEUNG APPLE - verify Alert button color is grey");
		cmsMainPage.fnPMI();
		cmsMainPage.selectPatientByCaseNum( moe_case_no ); //HN08000026Z, LEUNG APPLE
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_3");
		if( psf.checkAlertBtn("grey") ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "nkda - before set to nkda, alert button is grey");
		}
		System.out.println("QAG_checkpoint_4 test_nkda() - PMI, HN08000026Z, LEUNG APPLE - verify date string after NKDA checkbox is clicked");
		alertPanel = psf.clickAlertBtn();
		WebElement chkBx_NKDA = alertPanel.getChkBoxNKDA();
		chkBx_NKDA.click();
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_4");
		String check_today_str = shared_functions.getDateIn("d-M-yyyy");
		String strNKDADate = alertPanel.getDateStringNKDA().getText();
		if( strNKDADate.contains(check_today_str) ) {
	    	steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_4", "nkda - 'verified on' date match today's date");
		}
		alertPanel.closeAlertPanel();
		System.out.println("QAG_checkpoint_5 test_nkda() - PMI, HN08000026Z, LEUNG APPLE - re-verify date string ");
		alertPanel = psf.clickAlertBtn();
		WebElement BtnReVerifyNKDA = alertPanel.getBtnReVerifyNKDA();
		BtnReVerifyNKDA.click();
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_5");
		if( BtnReVerifyNKDA.isEnabled() == false ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_5", "re-verify nkda - the re-verify nkda button becomes disable");
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_5", "re-verify nkda - the re-verify nkda button did not become disable");
		}
		alertPanel.closeAlertPanel();
		System.out.println("test_nkda() - END");
	}
	public void test_allergy() throws Exception {
		System.out.println("test_allergy() - START");
		alertPanel = psf.clickAlertBtn();
		//alertPanel.addAllergy(dict);
		alertPanel.getAddAllergyBtn().click();
		apAllergen.prepareParamForAddAllergy(dict);
		apAllergen.addAllergy();
		System.out.println("QAG_checkpoint_6 test_allergy() - verify Allergy AMMONIUM CHLORIDE is added");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_6");
		Boolean isAllergyElementAdded = alertPanel.getAllergyElement().isDisplayed();
		if( isAllergyElementAdded ) {   
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_6", "add allergy - added allergy found on screen");
		}
		alertPanel.closeAlertPanel();
		System.out.println("QAG_checkpoint_7 test_allergy() - verify Alert Btn color is red");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_7");
		if( psf.checkAlertBtn("red") ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_7", "add allergy - spio alert button is red");
		}
		alertPanel = psf.clickAlertBtn();
		//alertPanel.editAllergy();
		alertPanel.getAllergyElement().click();
		alertPanel.getEditAllergyBtn().click();
		apAllergen.prepareParamForEditAllergy();
		apAllergen.editAllergy();
		String strClinical = "#gridAllergyRow0manifestations_display";
		String test_clinical = "Eczema";
		String strAddInfo = "#gridAllergyRow0remark";
		String test_additional_info = "FPS edit allergy";
		System.out.println("QAG_checkpoint_8 test_allergy() - verify Allergy Asthma -> Eczema");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_8");
		Boolean isEczema = alertPanel.isCorrectAllergenElement(strClinical,test_clinical);
		Boolean isAddInfoCorrect = alertPanel.isCorrectAllergenElement(strAddInfo, test_additional_info);
		if( isEczema&&isAddInfoCorrect ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_8", "edit allergy - modified [clinical manifestation and addition info] match");
		}
		//alertPanel.deleteAllergy();
		alertPanel.getAllergyElement().click();
		alertPanel.getDeleteAllergyBtn().click();
		apAllergen.prepareParamForDeleteAllergy();
		apAllergen.deleteAllergy();

		System.out.println("QAG_checkpoint_9 test_allergy() - verify Allergy is deleted");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_9");
		isEczema = alertPanel.isCorrectAllergenElement(strClinical,test_clinical);
		isAddInfoCorrect = alertPanel.isCorrectAllergenElement(strAddInfo, test_additional_info);
		if( ! (isEczema&&isAddInfoCorrect) ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_9", "del allergy - allergy record in [edit allergy] is removed");
		}
		alertPanel.closeAlertPanel();
		System.out.println("QAG_checkpoint_10 test_allergy() - verify Alert Btn color is grey");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_10");
		if( psf.checkAlertBtn("grey") ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_10", "del allergy - grey spio alert button is found");
		}else {
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_10", "del allergy - grey spio alert button is NOT found");
		}
		System.out.println("test_allergy() - END");
	}
	public void test_nondrug_allergy() throws Exception {	
		System.out.println("test_nondrug_allergy() - START");
		alertPanel = psf.clickAlertBtn();
		//alertPanel.addNonDrugAllergy(dict);
		alertPanel.getAddAllergyBtn().click();
		apAllergen.prepareParamForAddNonDrugAllergy(dict);
		apAllergen.addNonDrugAllergy();
		System.out.println("QAG_checkpoint_11 test_nondrug_allergy() - verify NDALLEGY is added");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_11");
		Boolean isNonDrugAllergyElementAdded = alertPanel.getAllergyElement().isDisplayed();
		if( isNonDrugAllergyElementAdded ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoing_11", "add nondrug allergy - added allergy found on screen");
		}
		//alertPanel.editNonDrugAllergy();
		alertPanel.getAllergyElement().click();
		alertPanel.getEditAllergyBtn().click();
		apAllergen.prepareParamForEditNonDrugAllergy();
		apAllergen.editAllergy();
		String strNonDrugClinical = "#gridAllergyRow0manifestations_display";
		String test_nondrug_clinical = "Eczema";
		String strNonDrugAddInfo = "#gridAllergyRow0remark";
		String test_nondrug_additional_info = "FPS edit nondrug";
		System.out.println("QAG_checkpoint_12 test_nondrug_allergy() - verify Allergy Asthma -> Eczema");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_12");
		Boolean isNonDrugEczema = alertPanel.isCorrectAllergenElement(strNonDrugClinical,test_nondrug_clinical);
		Boolean isNonDrugAddInfoCorrect = alertPanel.isCorrectAllergenElement( strNonDrugAddInfo, test_nondrug_additional_info);
		if( isNonDrugEczema&&isNonDrugAddInfoCorrect ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_12", "edit nondrug allergy - modified [clinical manifestation and addition info] match");
		}
		//alertPanel.deleteAllergy();
		alertPanel.getAllergyElement().click();
		alertPanel.getDeleteAllergyBtn().click();
		apAllergen.prepareParamForDeleteAllergy();
		apAllergen.deleteAllergy();
		System.out.println("QAG_checkpoint_13 test_nondrug_allergy() - verify Allergy is deleted");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_13");
		isNonDrugEczema = alertPanel.isCorrectAllergenElement(strNonDrugClinical,test_nondrug_clinical);
		isNonDrugAddInfoCorrect = alertPanel.isCorrectAllergenElement( strNonDrugAddInfo, test_nondrug_additional_info);
		if( ! (isNonDrugEczema&&isNonDrugAddInfoCorrect) ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_13", "del nondrug allergy - allergy record in [edit nondrug allergy] is removed");
		}
		alertPanel.closeAlertPanel();
		System.out.println("test_nondrug_allergy() - END");
	}
	public void test_advdrug_allergy() throws Exception {	
		System.out.println("test_advdrug_allergy() - START");
		alertPanel = psf.clickAlertBtn();
		//alertPanel.addADR(dict);
		alertPanel.getAddAdrBtn().click();
		apADR.prepareParamForAddADR(dict);
		apADR.addADR();
		System.out.println("QAG_checkpoint_14 test_advdrug_allergy() - verify ADR is added");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_14");
		Boolean isAddedADRElement = alertPanel.getADRElement().isDisplayed();
		Boolean isADRname = alertPanel.isCorrectADRElement("#gridAdrRow0adr_drug_name",dict.get("adv_drug_fullname"));
		if( isAddedADRElement ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_14", "add adverse drug allergy - added allergy found on screen");
		}
		//alertPanel.editADR();
		alertPanel.getADRElement().click();
		alertPanel.getEditAdrBtn().click();
		apADR.prepareParamForEditADR();
		apADR.editADR();
		String strADRReactionsDisplay = "#gridAdrRow0reactions_display";
		String test_ADR_clinical = dict.get("updated_adv_drug_reaction");
		String strADRAddInfo = "#gridAdrRow0remark";
		String test_advdrug_additional_info = "FPS edit adverse drug";
		System.out.println("QAG_checkpoint_15 test_advdrug_allergy() - verify ADR edited");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_15");
		Boolean isADRmodified = alertPanel.isCorrectADRElement(strADRReactionsDisplay,test_ADR_clinical);
		Boolean isADRAddInfoCorrect = alertPanel.isCorrectADRElement( strADRAddInfo, test_advdrug_additional_info);
		if( isADRmodified&&isADRAddInfoCorrect ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_15", "edit adverse drug allergy - modified [clinical manifestation and addition info] match");
		}
		//alertPanel.deleteADR();
		alertPanel.getADRElement().click();
		alertPanel.getDeleteAdrBtn().click();
		apADR.prepareParamForDeleteADR();
		apADR.deleteADR();
		System.out.println("QAG_checkpoint_16 test_advdrug_allergy() - verify ADR is deleted");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_16");
		isADRmodified = alertPanel.isCorrectADRElement(strADRReactionsDisplay,test_ADR_clinical);
		isADRAddInfoCorrect = alertPanel.isCorrectADRElement( strADRAddInfo, test_advdrug_additional_info);
		if( ! (isADRmodified&&isADRAddInfoCorrect) ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_16", "del adverse drug allergy - allergy record in [edit adverse drug allergy] is removed");
		}
		
		alertPanel.closeAlertPanel();
		
		System.out.println("test_advdrug_allergy() - END");
	}
	public void test_non_classified_alert() throws Exception {	
		System.out.println("test_non_classified_alert() - START");
		alertPanel = psf.clickAlertBtn();
		//alertPanel.addAlert(dict);
		alertPanel.getAddAlertBtn().click();
		apAlert.prepareParamForAddAlert(dict);
		apAlert.addAlert();
		System.out.println("QAG_checkpoint_17 test_non_classified_alert() - verify Alert is added");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_17");
		Boolean isNonClassifiedAlertCorrect = alertPanel.isCorrectAlertElement("#gridAlertRow0display_alert", dict.get("non_classified_alert_condition"));
		if( isNonClassifiedAlertCorrect ){
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_17", "add non-classified alert - added alert found on screen");
		}
		//alertPanel.editAlert();
		alertPanel.getAlertElement().click();
		alertPanel.getEditAlertBtn().click();
		apAlert.prepareParamForEditAlert();
		apAlert.editAlert();
		System.out.println("QAG_checkpoint_18 test_non_classified_alert() - verify Alert edited");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_18");
		Boolean isAlertModified = alertPanel.isCorrectAlertElement( "#gridAlertRow0remark", "FPS edit non-classified alert" );
		if( isAlertModified ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_18", "edit non-classified alert - modified [addition info] match");
		}			
		//alertPanel.deleteAlert();
		alertPanel.getAlertElement().click();
		alertPanel.getDeleteAlertBtn().click();
		apAlert.prepareParamForDeleteAlert();
		apAlert.deleteAlert();
		System.out.println("QAG_checkpoint_19 test_non_classified_alert() - verify Alert is deleted");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_19");
		isAlertModified = alertPanel.isCorrectAlertElement( "#gridAlertRow0display_alert" , dict.get("non_classified_alert_condition") );
		if( !isAlertModified ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_19", "del non-classified alert - alert record in [edit non-classified alert] is removed");
		}
		
		alertPanel.closeAlertPanel();
		System.out.println("test_non_classified_alert() - END");		
	}
	public void test_structured_classified_alert() throws Exception {
		System.out.println("test_structured_classified_alert() - START");
		alertPanel = psf.clickAlertBtn();
		//alertPanel.addClassifiedAlert(dict);
		alertPanel.getAddAlertBtn().click();
		apAlert.prepareParamForAddClassifiedAlert(dict);
		apAlert.addClassifiedAlert();
		String strAlertDisplayAlert = "#gridAlertRow0display_alert";
		String strAlertCondition = dict.get("structured_alert_condition");		
		System.out.println("QAG_checkpoint_20 test_structured_classified_alert() - verify Alert is added");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_20");
		Boolean isAlertTextCorrect = alertPanel.isCorrectAlertElement(strAlertDisplayAlert, strAlertCondition );
		if(  isAlertTextCorrect ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_20", "add structured classified alert - added alert found on screen within expected region");
		}			
		//alertPanel.deleteAlert();
		alertPanel.getAlertElement().click();
		alertPanel.getDeleteAlertBtn().click();
		apAlert.prepareParamForDeleteAlert();
		apAlert.deleteAlert();
		System.out.println("QAG_checkpoint_21 test_structured_classified_alert() - verify Alert is deleted");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_21");
		isAlertTextCorrect = alertPanel.isCorrectAlertElement(strAlertDisplayAlert, strAlertCondition);
		if( !isAlertTextCorrect ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_21", "del structured classified alert - alert record in is removed");
		}
			
		alertPanel.closeAlertPanel();
		System.out.println("test_structured_classified_alert() - END");		
	}
	public void test_freetext_classified_alert() throws Exception {	
		System.out.println("test_freetext_classified_alert() - START");
		alertPanel = psf.clickAlertBtn();
		//alertPanel.addFreeTextAlert(dict);
		alertPanel.getAddAlertBtn().click();
		apAlert.prepareParamForAddFreeTextAlert(dict);
		apAlert.addFreeTextAlert();
		String strFreeTextAlertDisplayAlert = "#gridAlertRow0display_alert";
		String strFreeTextAlert = dict.get("freetext_classified_alert");
		System.out.println("QAG_checkpoint_22 test_freetext_classified_alert() - verify Alert is added");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_22");
		Boolean isExistFreeTextAlert = alertPanel.isCorrectAlertElement(strFreeTextAlertDisplayAlert,strFreeTextAlert);
		Boolean isAlertAddInfoCorrect = alertPanel.isCorrectAlertElement("#gridAlertRow0remark", "fps free-text alert");
		if(  isExistFreeTextAlert && isAlertAddInfoCorrect ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_22", "add freetext classified alert - added alert found on screen within expected region");
		}
		//alertPanel.deleteAlert();
		alertPanel.getAlertElement().click();
		alertPanel.getDeleteAlertBtn().click();
		apAlert.prepareParamForDeleteAlert();
		apAlert.deleteAlert();
		System.out.println("QAG_checkpoint_23 test_freetext_classified_alert() - verify Alert is deleted");
		shared_functions.do_screen_capture_with_filename(driver, "T01S03_23");
		isExistFreeTextAlert = alertPanel.isCorrectAlertElement(strFreeTextAlertDisplayAlert,strFreeTextAlert);
		if( !isExistFreeTextAlert ) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_23", "del freetext classified alert - alert not found on screen");
		}
		alertPanel.closeAlertPanel();
		System.out.println("test_freetext_classified_alert() - END");		
	}	
	public void close_all_functions() throws InterruptedException {	
		System.out.println("close_all_functions() - START");
		cmsMainPage.fnNextPatient();
		System.out.println("close_all_functions() - END");
	}
	
	//POM design pattern
	class PAGE_AlertPanel {
		private WebDriver driver;	
		public PAGE_AlertPanel(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframeDetail(){
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("alertWinPanelDetailModalIFrame");				
		}
		public void switchToIframe() {
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("alertWinPanelModalIFrame");
		}
		public void closeDetailAlertPanel(){
			switchToIframeDetail();
			WebElement eCloseAlertPanelBtn = shared_functions.getElementWhenClickable(By.cssSelector("#btnCorpAllergyClose"));
			eCloseAlertPanelBtn.click();
		}
		public void closeAlertPanel(){
			switchToIframe();
			WebElement eCloseAlertPanelBtn = shared_functions.getElementWhenClickable(By.cssSelector("#btnCorpAllergyClose"));
			eCloseAlertPanelBtn.click();
		}
		/*
		public void switchToSpecificIframeByBtnClicked_TBD() {
			driver.switchTo().defaultContent(); 
			Boolean openByDetailBtnOfExistingAlertReminderWindow = (shared_functions.getElementsWhenVisible(By.cssSelector("iframe[name=alertWinPanelDetailModalIFrame]"))!=null);
			Boolean openByAlertBtn = (shared_functions.getElementsWhenVisible(By.cssSelector("iframe[name=alertWinPanelModalIFrame]"))!=null);
			if( openByDetailBtnOfExistingAlertReminderWindow ) {
				switchToIframeDetail();
			}else if( openByAlertBtn ){
				switchToIframe();				
			}
		}
		public void closeAlertPanel_TBD(){
			switchToSpecificIframeByBtnClicked();
			WebElement eCloseAlertPanelBtn = shared_functions.getElementWhenClickable(By.cssSelector("#btnCorpAllergyClose"));
			eCloseAlertPanelBtn.click();
		}
		*/

		public void handleSameAlertErrorPopUpWindow(){
			switchToIframe();
			shared_functions.switchToFrameByString("cAllergyIFrameFnWin");
			if( shared_functions.getElementsWhenVisible(By.cssSelector("#cuibutton-1034"))!=null ) {
				WebElement eSavingSameAlertError = shared_functions.getElementWhenClickable(By.cssSelector("#cuibutton-1034"));
				eSavingSameAlertError.click();
				WebElement eAllergyInputScreenCancel = shared_functions.getElementWhenClickable(By.cssSelector("#btnAllergyCancel")); 
				eAllergyInputScreenCancel.click();
				WebElement eConfirmAbortChange = shared_functions.getElementWhenClickable(By.cssSelector("#cuibutton-1053")); 
				eConfirmAbortChange.click();
				System.out.println("Same Alert Existing");
			}else {
				System.out.println("No Same Alert Existing");
			}	
		}
		public WebElement getChkBoxNKDA() {
			switchToIframe();
			return shared_functions.getElementWhenClickable(By.cssSelector("#cbAllergyNKDA"));
		}
		public WebElement getDateStringNKDA() {
			switchToIframe();
			return shared_functions.getElementWhenClickable(By.cssSelector("#divCorpAllergyNKDAReverify font"));
		}
		public WebElement getBtnReVerifyNKDA(){
			switchToIframe();
			return shared_functions.getElementWhenClickable(By.cssSelector("#btnAllergyNkdaVerify"));			
		}		
		//Allergen
		public WebElement getAddAllergyBtn() {
			switchToIframe();
			WebElement e = shared_functions.getElementWhenClickable(By.cssSelector("#btnAllergyAdd"));
			return e;
		}
		public WebElement getEditAllergyBtn() {
			switchToIframe();
			WebElement e  = shared_functions.getElementWhenClickable(By.cssSelector("#btnAllergyEdit"));
			return e;
		}
		public WebElement getDeleteAllergyBtn() {
			switchToIframe();
			WebElement e  = shared_functions.getElementWhenClickable(By.cssSelector("#btnAllergyDelete"));
			return e;
		}
		public WebElement getAllergyElement() {
			switchToIframe();
			return shared_functions.getElementWhenClickable(By.cssSelector("#gridAllergyRow0allergen_name"));
		}
		public Boolean isExistAllergenElement(String strID) {
			switchToIframe();
			return (shared_functions.checkAndGetElementsWhenVisible(By.cssSelector(strID))!=null);
		}
		public Boolean isCorrectAllergenElement(String strID, String strCheck) {
			switchToIframe();
			if( isExistAllergenElement(strID) ) {
				Boolean isCorrect = shared_functions.getElementWhenClickable(By.cssSelector(strID)).getText().equals(strCheck);
				if(isCorrect) {
					return true;
				}
			}
			return false;
		}
		//ADR
		public WebElement getAddAdrBtn() {
			switchToIframe();
			WebElement e = shared_functions.getElementWhenClickable(By.cssSelector("#btnAdrAdd"));
			return e;
		}
		public WebElement getEditAdrBtn() {
			switchToIframe();
			WebElement e  = shared_functions.getElementWhenClickable(By.cssSelector("#btnAdrEdit"));
			return e;
		}
		public WebElement getDeleteAdrBtn() {
			switchToIframe();
			WebElement e  = shared_functions.getElementWhenClickable(By.cssSelector("#btnAdrDelete"));
			return e;
		}
		public WebElement getADRElement() {
			switchToIframe();
			return shared_functions.getElementWhenClickable(By.cssSelector( "#gridAdrRow0adr_drug_name" ));
		}
		public Boolean isExistADRElement(String strID) {
			switchToIframe();
			return shared_functions.checkAndGetElementsWhenVisible(By.cssSelector(strID))!=null;
		}
		public Boolean isCorrectADRElement(String strID, String strCheck) {
			switchToIframe();
			if( isExistADRElement(strID) ) {
				Boolean isCorrect = shared_functions.getElementWhenClickable(By.cssSelector(strID)).getText().equals(strCheck);
				if(isCorrect) {
					return true;
				}
			}
			return false;
		}
		//Alert
		public WebElement getAddAlertBtn() {
			switchToIframe();
			WebElement e = shared_functions.getElementWhenClickable(By.cssSelector("#btnAlertAdd"));
			return e;
		}
		public WebElement getEditAlertBtn() {
			switchToIframe();
			WebElement e  = shared_functions.getElementWhenClickable(By.cssSelector("#btnAlertEdit"));
			return e;
		}
		public WebElement getDeleteAlertBtn() {
			switchToIframe();
			WebElement e  = shared_functions.getElementWhenClickable(By.cssSelector("#btnAlertDelete"));
			return e;
		}
		public WebElement getAlertElement() {
			switchToIframe();
			return shared_functions.getElementWhenClickable(By.cssSelector( "#gridAlertRow0display_alert" ));
		}
		public Boolean isExistAlertElement(String strID) {
			switchToIframe();
			return shared_functions.checkAndGetElementsWhenVisible(By.cssSelector(strID))!=null;
		}
		public Boolean isCorrectAlertElement(String strID, String strCheck) {
			switchToIframe();
			if( isExistAlertElement(strID) ) {
				Boolean isCorrect = shared_functions.getElementWhenClickable(By.cssSelector(strID)).getText().equals(strCheck);
				if(isCorrect) {
					return true;
				}
			}
			return false;
		}
		
	}
	class PAGE_AlertPanel_Allergen{
		protected WebDriver driver;
		public PAGE_AlertPanel_Allergen(WebDriver driver) {
			this.driver = driver;
		}
		private String param_TextArea_Allergen; 
		private String param_List_Allergen; 
		private String param_checkChkBx_Allergen;
		private String param_uncheckChkBx_Allergen;
		private String param_radioBtn_Allergen;
		private String param_textAreaAdditionalInformation_Allergen;
		private String param_DeleteReasonChkBx_Allergen;
		private String param_DeleteReasonTextArea_Allergen;
		private String param_NoAllergenFoundOption_Allergen;
		public void prepareParamForAddAllergy(Map dict) {
			param_TextArea_Allergen = dict.get("allergy_add_keyword").toString(); //CHLO 
			param_List_Allergen = "#gridAllergyDlRow3formatted_drug"; //AMMONIUM_CHLORIDE 
			param_checkChkBx_Allergen = "#gridAllergyMlRow3manifestation_desc input[type=checkbox]"; //Asthma 
			param_radioBtn_Allergen = "#rbCertaintyC"; //Certain 
			//rbCertaintyS // Suspected
		}
		public void addAllergy() throws InterruptedException {
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("alertWinPanelModalIFrame");
			shared_functions.switchToFrameByString("cAllergyIFrameFnWin");
			WebElement eAllergyKeywordTextArea = shared_functions.getElementWhenClickable(By.cssSelector("#tfAllergyKeyword"));
			eAllergyKeywordTextArea.sendKeys( param_TextArea_Allergen );
			shared_functions.clickBtnByCssWithException(param_List_Allergen);
			//WebElement eAllergenList = shared_functions.getElementWhenClickable(By.cssSelector( param_List_Allergen ));
			//eAllergenList.click();
			WebElement eClinicalManifestation_Asthma = shared_functions.getElementWhenClickable(By.cssSelector( param_checkChkBx_Allergen ));
			eClinicalManifestation_Asthma.click();
			WebElement eLevelOfCertainty_Certain = shared_functions.getElementWhenClickable(By.cssSelector( param_radioBtn_Allergen ));
			eLevelOfCertainty_Certain.click();
			WebElement eAllergyInputScreenAddBtn = shared_functions.getElementWhenClickable(By.cssSelector("#divCorpAllergyAllergenAddBtn #btnAllergyAdd"));
			eAllergyInputScreenAddBtn.click();
		}
		public void prepareParamForAddNonDrugAllergy(Map dict) {
			param_TextArea_Allergen = dict.get("nondrug_text").toString(); //NDALLEGY 
			param_NoAllergenFoundOption_Allergen = "#freeTextOpt3";
			param_checkChkBx_Allergen= "#gridAllergyMlRow3manifestation_desc input[type=checkbox]";;
			param_radioBtn_Allergen= "#rbCertaintyC";
		}
		public void addNonDrugAllergy(){
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("alertWinPanelModalIFrame");
			shared_functions.switchToFrameByString("cAllergyIFrameFnWin");
			WebElement eAllergyKeywordTextArea = shared_functions.getElementWhenClickable(By.cssSelector("#tfAllergyKeyword"));
			eAllergyKeywordTextArea.sendKeys( param_TextArea_Allergen );
			WebElement eAllergyInputScreenAddBtn = shared_functions.getElementWhenClickable(By.cssSelector("#divCorpAllergyAllergenAddBtn #btnAllergyAdd"));
			eAllergyInputScreenAddBtn.click();
			WebElement eEnterFreeTextNonDrugAllergy = shared_functions.getElementWhenClickable(By.cssSelector( param_NoAllergenFoundOption_Allergen )); 
			eEnterFreeTextNonDrugAllergy.click();
			WebElement eClinicalManifestation_Asthma = shared_functions.getElementWhenClickable(By.cssSelector( param_checkChkBx_Allergen ));
			eClinicalManifestation_Asthma.click();
			WebElement eLevelOfCertainty_Certain = shared_functions.getElementWhenClickable(By.cssSelector( param_radioBtn_Allergen ));
			eLevelOfCertainty_Certain.click();
			eAllergyInputScreenAddBtn.click();
		}
		public void prepareParamForEditAllergy() {
			param_checkChkBx_Allergen = "#gridAllergyMlRow4manifestation_desc input[type=checkbox]"; //Eczema
			param_uncheckChkBx_Allergen = "#gridAllergyMlRow3manifestation_desc input[type=checkbox]";//Asthma
			param_textAreaAdditionalInformation_Allergen = "FPS edit allergy";		
		}
		public void prepareParamForEditNonDrugAllergy() {
			param_checkChkBx_Allergen = "#gridAllergyMlRow4manifestation_desc input[type=checkbox]"; //Eczema
			param_uncheckChkBx_Allergen = "#gridAllergyMlRow3manifestation_desc input[type=checkbox]";//Asthma
			param_textAreaAdditionalInformation_Allergen = "FPS edit nondrug";		
		}
		public void editAllergy() {
			shared_functions.switchToFrameByString("cAllergyIFrameFnWin");
			WebElement eClinicalManifestation_Eczema = shared_functions.getElementWhenClickable(By.cssSelector( param_checkChkBx_Allergen )); //Eczema
			eClinicalManifestation_Eczema.click();
			WebElement eClinicalManifestation_Asthma = shared_functions.getElementWhenClickable(By.cssSelector( param_uncheckChkBx_Allergen ));
			eClinicalManifestation_Asthma.click();
			WebElement eAdditionalInformation = shared_functions.getElementWhenClickable(By.cssSelector("#taAllergyAddInfo"));
			eAdditionalInformation.sendKeys( param_textAreaAdditionalInformation_Allergen );
			WebElement eAllergyEditSreenAddBtn = shared_functions.getElementWhenClickable(By.cssSelector("#divCorpAllergyAllergenAddBtn #btnAllergyAdd"));
			eAllergyEditSreenAddBtn.click();
		}
		public void prepareParamForDeleteAllergy() {
			param_DeleteReasonChkBx_Allergen = "#gridDelReasonRow2reason_desc input";//This Entry is not for this patient
			param_DeleteReasonTextArea_Allergen = "FPS testing del allergy";
		}
		public void deleteAllergy() {
			WebElement eDeleteReasonChkBx = shared_functions.getElementWhenClickable(By.cssSelector( param_DeleteReasonChkBx_Allergen ));
			eDeleteReasonChkBx.click();
			WebElement eDeleteReasonTxtArea = shared_functions.getElementWhenClickable(By.cssSelector("#taDelRemark"));
			eDeleteReasonTxtArea.sendKeys( param_DeleteReasonTextArea_Allergen );
			WebElement eProceedBtn = shared_functions.getElementWhenClickable(By.cssSelector("#buttonDeleteProceed"));
			eProceedBtn.click();
		}
	}
	class PAGE_AlertPanel_ADR {
		private WebDriver driver;
		public PAGE_AlertPanel_ADR(WebDriver driver) {
			this.driver = driver;
		}
		private String param_KeywordsTextArea_ADR;
		private String param_Keywords_ADR;
		private String param_KeywordsList_ADR;
		private String param_checkChkBx_ADR;
		private String param_clickLevelRadioBtn_ADR;
		private String param_saveBtn_ADR;
		
		private String param_itemToEdit_ADR;
		private String param_uncheckChkBx_ADR;
		private String param_AdditionalInformation_ADR;
		private String param_AdditionalInformationTextArea_ADR;
		
		private String param_itemToDelete_ADR;
		private String param_DeleteReasonChkBx_ADR;
		private String param_DeleteReasonTextArea_ADR;
		
		public void prepareParamForAddADR(Map dict) {
			param_KeywordsTextArea_ADR = "#tfAdrKeyword";
			param_Keywords_ADR = dict.get("adv_drug_keyword").toString(); //TEST
			param_KeywordsList_ADR = "#gridAdrDlRow4formatted_drug"; //TESTOSTERONE PROPIONATE
			param_checkChkBx_ADR = "#gridAdrReactionRow24side_effect_desc input[type=checkbox]"; //Irregular Menstrual Periods
			//param_checkChkBx_ADR
			//gridAdrReactionRow4side_effect_desc - Amenorrhea
			//gridAdrReactionRow6side_effect_desc - Bladder Irritability
			//gridAdrReactionRow15side_effect_desc - Gynecomastia
			//#gridAdrReactionRow24side_effect_desc - Irregular Menstrual Periods
			//gridAdrReactionRow28side_effect_desc - Mastalgia
			//gridAdrReactionRow31side_effect_desc - Priapism
			//gridAdrReactionRow34side_effect_desc - Urinary Tract Infections
			//gridAdrReactionRow35side_effect_desc - Virilism
			param_clickLevelRadioBtn_ADR = "#rbSeverityS"; //Severe 
			//rbSeverityM - Mild
			param_saveBtn_ADR = "#btnAdrAdd";
			 
		}
		public void addADR() throws InterruptedException {
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("alertWinPanelModalIFrame");
			shared_functions.switchToFrameByString("cAllergyIFrameFnWin");
			WebElement eKeywordTextArea = shared_functions.getElementWhenClickable(By.cssSelector(param_KeywordsTextArea_ADR));
			eKeywordTextArea.sendKeys( param_Keywords_ADR );
			shared_functions.clickBtnByCssWithException(param_KeywordsList_ADR);
			//WebElement eKeywordList = shared_functions.getElementWhenClickable(By.cssSelector(param_KeywordsList_ADR));
			//eKeywordList.click();
			WebElement eCheckChkBx = shared_functions.getElementWhenClickable(By.cssSelector( param_checkChkBx_ADR ));
			eCheckChkBx.click();
			WebElement eLevelRadioBtn = shared_functions.getElementWhenClickable(By.cssSelector( param_clickLevelRadioBtn_ADR ));
			eLevelRadioBtn.click();
			WebElement eSaveBtn = shared_functions.getElementWhenClickable(By.cssSelector( param_saveBtn_ADR ));
			eSaveBtn.click();
		}
		
		public void prepareParamForEditADR() {
			param_checkChkBx_ADR = "#gridAdrReactionRow28side_effect_desc input[type=checkbox]"; //Mastalgia
			param_uncheckChkBx_ADR = "#gridAdrReactionRow24side_effect_desc input[type=checkbox]"; //Irregular Menstrual Periods
			param_AdditionalInformationTextArea_ADR = "#taAdrAddInfo";
			param_AdditionalInformation_ADR = "FPS edit adverse drug";
			param_saveBtn_ADR ="#btnAdrSave";
		}
		public void editADR() {	
			shared_functions.switchToFrameByString("cAllergyIFrameFnWin");
			WebElement eCheckChkBx = shared_functions.getElementWhenClickable(By.cssSelector( param_checkChkBx_ADR )); 
			eCheckChkBx.click();
			WebElement eUncheckChkBx = shared_functions.getElementWhenClickable(By.cssSelector( param_uncheckChkBx_ADR ));
			eUncheckChkBx.click();
			WebElement eAdditionalInformation = shared_functions.getElementWhenClickable(By.cssSelector( param_AdditionalInformationTextArea_ADR ));
			eAdditionalInformation.sendKeys( param_AdditionalInformation_ADR );
			WebElement eSaveBtn = shared_functions.getElementWhenClickable(By.cssSelector( param_saveBtn_ADR ));
			eSaveBtn.click();
		}

		public void prepareParamForDeleteADR() {
			param_DeleteReasonChkBx_ADR = "#gridDelReasonRow2reason_desc input";//This Entry is not for this patient
			//
			param_DeleteReasonTextArea_ADR = "FPS testing del adr drug";
		}
		public void deleteADR() {
			WebElement eDeleteReasonChkBx = shared_functions.getElementWhenClickable(By.cssSelector( param_DeleteReasonChkBx_ADR ));
			eDeleteReasonChkBx.click();
			WebElement eDeleteReasonTxtArea = shared_functions.getElementWhenClickable(By.cssSelector("#taDelRemark"));
			eDeleteReasonTxtArea.sendKeys( param_DeleteReasonTextArea_ADR );
			WebElement eProceedBtn = shared_functions.getElementWhenClickable(By.cssSelector("#buttonDeleteProceed"));
			eProceedBtn.click();
		}
	}
	class PAGE_AlertPanel_Alert {
		private WebDriver driver;
		public PAGE_AlertPanel_Alert(WebDriver driver) {this.driver = driver;}
		private String param_selectAlertCategoriesList_Alert;
		private String param_KeywordsTextArea_Alert;
		private String param_Keywords_Alert;
		private String param_checkChkBx_Alert;
		private String param_inputFreeTextBtn_Alert;
		private String param_saveBtn_Alert;
		private String param_AdditionalInformation_Alert;
		private String param_AdditionalInformationTextArea_Alert;
		
		public void prepareParamForAddAlert(Map dict) {
			param_selectAlertCategoriesList_Alert = "#gridAlertCatRow0category_desc"; //Clinical Condition
			param_KeywordsTextArea_Alert = "#tfAlertKeyword";
			param_Keywords_Alert = "";
			param_checkChkBx_Alert = "#gridAlertItem1Row0alert_desc input[type=checkbox]"; //G6PD Deficiency
			param_AdditionalInformationTextArea_Alert = "#taAlertAddInfo";
			param_AdditionalInformation_Alert = "";
			param_saveBtn_Alert = "#btnAlertAdd";		 
		}
		public void addAlert(){
			//PAGE_AlertPanel.eBtnAlertAdd.click();
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("alertWinPanelModalIFrame");
			shared_functions.switchToFrameByString("cAllergyIFrameFnWin");
			WebElement eAlertCategoriesList = shared_functions.getElementWhenClickable(By.cssSelector( param_selectAlertCategoriesList_Alert ));
			eAlertCategoriesList.click();
			WebElement eKeywordTextArea = shared_functions.getElementWhenClickable(By.cssSelector(param_KeywordsTextArea_Alert));
			eKeywordTextArea.sendKeys( param_Keywords_Alert );
			WebElement eCheckChkBx = shared_functions.getElementWhenClickable(By.cssSelector( param_checkChkBx_Alert ));
			eCheckChkBx.click();
			WebElement eAdditionalInformationTextArea = shared_functions.getElementWhenClickable(By.cssSelector( param_AdditionalInformationTextArea_Alert ));
			eAdditionalInformationTextArea.sendKeys( param_AdditionalInformation_Alert );		
			WebElement eSaveBtn = shared_functions.getElementWhenClickable(By.cssSelector( param_saveBtn_Alert ));
			eSaveBtn.click();
		}
		
		public void prepareParamForAddClassifiedAlert(Map dict) {
			param_selectAlertCategoriesList_Alert = "#gridAlertCatRow3category_desc";  //Infectious
			param_KeywordsTextArea_Alert = "#tfAlertKeyword";
			param_Keywords_Alert = "";
			param_checkChkBx_Alert = "#gridAlertItem1Row2alert_desc input[type=checkbox]"; //HIV Carrier
			param_AdditionalInformationTextArea_Alert = "#taAlertAddInfo";
			param_AdditionalInformation_Alert = "";
			param_saveBtn_Alert = "#btnAlertAdd";		 
		}
		public void addClassifiedAlert(){
			//PAGE_AlertPanel.eBtnAlertAdd.click();
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("alertWinPanelModalIFrame");
			shared_functions.switchToFrameByString("cAllergyIFrameFnWin");
			WebElement eAlertCategoriesList = shared_functions.getElementWhenClickable(By.cssSelector( param_selectAlertCategoriesList_Alert ));
			eAlertCategoriesList.click();
			WebElement eKeywordTextArea = shared_functions.getElementWhenClickable(By.cssSelector( param_KeywordsTextArea_Alert ));
			eKeywordTextArea.sendKeys( param_Keywords_Alert );
			WebElement eCheckChkBx = shared_functions.getElementWhenClickable(By.cssSelector( param_checkChkBx_Alert ));
			eCheckChkBx.click();
			WebElement eAdditionalInformationTextArea = shared_functions.getElementWhenClickable(By.cssSelector( param_AdditionalInformationTextArea_Alert ));
			eAdditionalInformationTextArea.sendKeys( param_AdditionalInformation_Alert );		
			WebElement eSaveBtn = shared_functions.getElementWhenClickable(By.cssSelector( param_saveBtn_Alert ));
			eSaveBtn.click();
		}
		
		public void prepareParamForAddFreeTextAlert(Map dict) {
			param_KeywordsTextArea_Alert = "#tfAlertKeyword";
			param_Keywords_Alert = dict.get("freetext_classified_alert").toString(); //freetex_alert
			param_inputFreeTextBtn_Alert = "#panelAlertCatItem font.fontBlueUnderLine";
			param_AdditionalInformationTextArea_Alert = "#taAlertAddInfo";
			param_AdditionalInformation_Alert = "fps free-text alert";
			param_saveBtn_Alert = "#btnAlertAdd";		 
		}
		public void addFreeTextAlert() throws InterruptedException {
			//PAGE_AlertPanel.eBtnAlertAdd.click();
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("alertWinPanelModalIFrame");
			shared_functions.switchToFrameByString("cAllergyIFrameFnWin");
			WebElement eKeywordTextArea = shared_functions.getElementWhenClickable(By.cssSelector( param_KeywordsTextArea_Alert ));
			eKeywordTextArea.sendKeys( param_Keywords_Alert );
			shared_functions.clickBtnByCssWithException(param_inputFreeTextBtn_Alert);
			//WebElement eInputFreeTextBtn = shared_functions.getElementWhenClickable(By.cssSelector( param_inputFreeTextBtn_Alert ));
			//eInputFreeTextBtn.click();
			WebElement eAdditionalInformationTextArea = shared_functions.getElementWhenClickable(By.cssSelector( param_AdditionalInformationTextArea_Alert ));
			eAdditionalInformationTextArea.sendKeys( param_AdditionalInformation_Alert );		
			WebElement eSaveBtn = shared_functions.getElementWhenClickable(By.cssSelector( param_saveBtn_Alert ));
			eSaveBtn.click();
		}
		
		public void prepareParamForEditAlert() {
			param_AdditionalInformationTextArea_Alert = "#taAlertAddInfo";
			param_AdditionalInformation_Alert = "FPS edit non-classified alert";
			param_saveBtn_Alert ="#btnAlertAdd";
		}
		public void editAlert() {
			shared_functions.switchToFrameByString("cAllergyIFrameFnWin");
			shared_functions.clickBtnByCssWithException(param_AdditionalInformationTextArea_Alert);
			WebElement eAdditionalInformation = shared_functions.getElementWhenClickable(By.cssSelector( param_AdditionalInformationTextArea_Alert ));
			eAdditionalInformation.sendKeys( param_AdditionalInformation_Alert );
			WebElement eSaveBtn = shared_functions.getElementWhenClickable(By.cssSelector( param_saveBtn_Alert ));
			eSaveBtn.click();
		}

		public void prepareParamForDeleteAlert() {

		}
		public void deleteAlert() {
			WebElement eConfirmDeleteBtn = shared_functions.getElementsWhenVisible(By.cssSelector("a.x-btn-focus.x-btn-cui-small-focus")).get(0);
			eConfirmDeleteBtn.click();
		}
	}
	

}

