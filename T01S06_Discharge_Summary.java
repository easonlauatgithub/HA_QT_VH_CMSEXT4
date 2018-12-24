package suite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class T01S06_Discharge_Summary {
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_DischargeSummary patientDetailPageDS = null;
	int steps_passed;
	int total_steps;
	Map<String, String> dict = new HashMap<>();
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T01S06_Discharge_Summary";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T01S06_extjs4");
		dict = shared_functions.sam_gor();

		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		patientDetailPageDS = new PAGE_PatientDetailPage_DischargeSummary(driver);
		steps_passed = 0;
		total_steps = 5;
		//driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);

		cmsMainPage.fnDischargeSummary();
		cmsMainPage.selectPatientByCaseNum( dict.get("case_no") );
		psf.closeExistingAlertReminderWindow();
		
		test_create_ds();
		test_retrieve_all_previous();
		test_update_ds();
		test_add_ds_from_tmpl();
		test_delete_ds();

		shared_functions.countTestPassed(steps_passed, total_steps);
		cmsMainPage.fnNextPatient();
	} 
	public void test_create_ds() throws Exception {
		System.out.println("test_create_ds() - START");
		patientDetailPageDS.handleAutoSavePopUpWindow();
		WebElement eTxtAreaPlain = patientDetailPageDS.getTxtAreaPlain();
		eTxtAreaPlain.sendKeys( dict.get("create_ds_zoomtext") );
		WebElement eTxtAreaPlanOfManagement = patientDetailPageDS.getTxtAreaPlanOfManagement();
		eTxtAreaPlanOfManagement.sendKeys( dict.get("create_ds_manangetext") );
		patientDetailPageDS.addRemark( dict.get("create_ds_remarks") );
		patientDetailPageDS.clickSave();
		cmsMainPage.fnNextPatient();
		
		cmsMainPage.fnDischargeSummary();
		cmsMainPage.selectPatientByCaseNum( dict.get("case_no") );
		//cmsMainPage.handleSelectPatientSpecialtyPopUpWindow();
		psf.closeExistingAlertReminderWindow();
		System.out.println("test_create_ds() - verify zoomtext managetext");
		shared_functions.do_screen_capture_with_filename(driver, "T01S06");		
		Boolean isCorrectZoomText =  patientDetailPageDS.getTxtAreaPlain().getText().equals(dict.get("create_ds_zoomtext"));
		Boolean isCorrectManageText =  patientDetailPageDS.getTxtAreaPlanOfManagement().getText().equals(dict.get("create_ds_manangetext"));
		if(isCorrectZoomText && isCorrectManageText) {
	    	steps_passed = steps_passed + 1;
	    	shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "create ds - zoomtext AND plan of management match");
		}
		System.out.println("test_create_ds() - SKIP PRINTING");
		//patientDetailPageDS.clickPrint();
		shared_functions.reporter_ReportEvent("SKIP", "PRINTING", "");		
		patientDetailPageDS.clickPreview();
		shared_functions.sleepForAWhile(10000); //wait for Preview iframe[DscnViewerApplet] to load
		System.out.println("test_create_ds() - SKIP PRINT IN PREVIEW");
		//patientDetailPageDS.clickPrintInPreview();
		patientDetailPageDS.clickCloseInPreview();
		shared_functions.reporter_ReportEvent("SKIP", "PREVIEW", "");
		System.out.println("test_create_ds() - END");
	}
	public void test_retrieve_all_previous() throws Exception {
		System.out.println("test_retrieve_all_previous() - START");
		patientDetailPageDS.clickConsultationList();
		String check_case_no = shared_functions.convert_case_no_to_cms_format(dict.get("case_no"));
		PAGE_ConsultationList cosultationList = new PAGE_ConsultationList(driver);
		cosultationList.clickCaseNo(check_case_no);
		Boolean isExistInfoWindow = cosultationList.verifyDesc();
		if(isExistInfoWindow) {
			cosultationList.clickOKBtn();
		}
		System.out.println("test_retrieve_all_previous() - verify zoomtext managetext again");
		shared_functions.do_screen_capture_with_filename(driver, "T01S06_2");
		WebElement eZoomText = shared_functions.getElementWhenVisible(By.cssSelector(cosultationList.cssZoomText));
		String strInCMS = eZoomText.getText();
		String strInExcel = dict.get("create_ds_zoomtext");
		Boolean isCorrectZoomText = strInCMS.equals(strInExcel);
		if(isCorrectZoomText) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "retrieve all previous notes - content same as 'create ds'");
		}
		patientDetailPageDS.clickZoomInConsultationList();
		System.out.println("test_retrieve_all_previous() - END");
	}
	public void test_update_ds() throws Exception {
		System.out.println("test_update_ds() - START");
		String use_zoomtext = dict.get("create_ds_zoomtext"); //QTP DSCN zoom text
		String use_managetext = dict.get("create_ds_manangetext"); //QTP script at plan of management
		String update_time = " updated at " + shared_functions.getTime();
		WebElement eTxtAreaPlain = patientDetailPageDS.getTxtAreaPlain();
		eTxtAreaPlain.clear();
		eTxtAreaPlain.sendKeys( use_zoomtext );
		eTxtAreaPlain.sendKeys( update_time );
		WebElement eTxtAreaPlanOfManagement = patientDetailPageDS.getTxtAreaPlanOfManagement();
		eTxtAreaPlanOfManagement.clear();
		eTxtAreaPlanOfManagement.sendKeys( use_managetext );
		eTxtAreaPlanOfManagement.sendKeys( update_time );
		use_zoomtext = use_zoomtext + update_time;
		use_managetext = use_managetext + update_time;
		patientDetailPageDS.clickSave();
		cmsMainPage.fnNextPatient();
		System.out.println("test_update_ds() - verify updated zoomtext managetext");	
		cmsMainPage.fnDischargeSummary();
		cmsMainPage.selectPatientByCaseNum( dict.get("case_no") );
		//cmsMainPage.handleSelectPatientSpecialtyPopUpWindow();
		psf.closeExistingAlertReminderWindow();
		shared_functions.do_screen_capture_with_filename(driver, "T01S06_4");
		Boolean isCorrectZoomText =  patientDetailPageDS.getTxtAreaPlain().getText().equals(use_zoomtext);
		Boolean isCorrectManageText =  patientDetailPageDS.getTxtAreaPlanOfManagement().getText().equals(use_managetext);
		if(isCorrectZoomText && isCorrectManageText) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_4", "update ds - zoomtext and plan of management match");
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_4", "update ds - zoomtext and plan of management not match");
		}
		System.out.println("test_update_ds() - END");
	}
	public void test_add_ds_from_tmpl() throws Exception {
		System.out.println("test_add_ds_from_tmpl() - START");
		WebElement eTxtAreaPlain = patientDetailPageDS.getTxtAreaPlain();
		eTxtAreaPlain.clear();
		patientDetailPageDS.clickTmpl();
		PAGE_NoteTemplate noteTemplate = new PAGE_NoteTemplate(driver);
		noteTemplate.waitUntilTemplateListFinishedLoading();
		noteTemplate.searchFor(dict.get("add_tmpl_header"));//QAG_TMPL
		noteTemplate.selectTemplate(0);
		patientDetailPageDS.clickSave();
		System.out.println("test_add_ds_from_tmpl() - verify zoomtext from template");
		shared_functions.do_screen_capture_with_filename(driver, "T01S06_3");
		String check_text = dict.get("add_tmpl_check").trim();
		String textInCms = patientDetailPageDS.getTxtAreaPlain().getText().trim();
		Boolean isCorrectZoomText =  textInCms.equals(check_text);
		if(isCorrectZoomText) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "add content from template - expected template text found");
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_3", "add content from template - expected template text not found");
		}
		System.out.println("test_add_ds_from_tmpl() - END");
	}
	public void test_delete_ds() throws Exception {
		System.out.println("test_delete_ds() - START");
		patientDetailPageDS.clickDelete();
		cmsMainPage.fnNextPatient();
		cmsMainPage.fnDischargeSummary();
		cmsMainPage.selectPatientByCaseNum( dict.get("case_no") );
		//cmsMainPage.handleSelectPatientSpecialtyPopUpWindow();		
		System.out.println("test_delete_ds() - verify zoomtext deleted");
		shared_functions.do_screen_capture_with_filename(driver, "T01S06_5");
		
		Boolean isCorrectZoomText =  patientDetailPageDS.getTxtAreaPlain().getText().equals("");
		Boolean isCorrectManageText =  patientDetailPageDS.getTxtAreaPlanOfManagement().getText().equals("");		
		if(isCorrectZoomText&&isCorrectManageText) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_5", "delete ds - after re-open function, zoomtext and plan of management empty");
		}
		System.out.println("test_delete_ds() - END");
	}
	class PAGE_PatientDetailPage_DischargeSummary {
		WebDriver driver = null;
		public PAGE_PatientDetailPage_DischargeSummary(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe() {
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("320Panel");			
		}
		public void handleAutoSavePopUpWindow() {
			switchToIframe();
			if( shared_functions.checkAndGetElementsWhenVisible(By.cssSelector("#cmsAutoSaveMainWin"))!=null ) {
				//shared_functions.getElementWhenClickable(By.cssSelector("#cmsAutoSaveMainWinYesBtn-frame1Table")).click();
				shared_functions.getElementWhenClickable(By.cssSelector("#cmsAutoSaveMainWinNoBtn-frame1Table")).click();
			}
		}
		public WebElement getTxtAreaPlain() {
			switchToIframe();
			return shared_functions.getElementWhenClickable(By.cssSelector("textarea[name=zoomTxtAreaPlain-inputEl]"));
		}
		public WebElement getTxtAreaPlanOfManagement() {
			switchToIframe();
			shared_functions.Hardcode(); //below throws JavascriptException
			WebElement e = shared_functions.getElementWhenClickable(By.cssSelector("textarea[name=zoomManagePlanTxtArea-inputEl]"));
			return e;
		}
		public void addRemark(String str) {
			switchToIframe();
			shared_functions.getElementWhenClickable(By.cssSelector("#zoomRemarkBtn")).click();
			shared_functions.getElementWhenClickable(By.cssSelector("#remarkTextArea-inputEl")).sendKeys(str);
			shared_functions.getElementWhenClickable(By.cssSelector("#remarkSaveBtn")).click();			
		}
		public void clickSave() {
			switchToIframe();
			shared_functions.getElementWhenClickable(By.cssSelector("#zoomSaveBtn")).click(); //Save
		}
		public void clickDelete() {
			switchToIframe();
			shared_functions.getElementWhenClickable(By.cssSelector("#zoomDelBtn")).click(); //Delete
			shared_functions.getElementsWhenVisible(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-btn-cui-small.x-noicon.x-btn-noicon.x-btn-cui-small-noicon")).get(0).click();
			shared_functions.getElementWhenClickable(By.cssSelector(".x-btn.x-unselectable.x-box-item.x-btn-cui-small.x-noicon.x-btn-noicon.x-btn-cui-small-noicon .x-frame-mc.x-btn-mc.x-btn-cui-small-mc.x-btn-cui-small-noicon-mc")).click();
		}
		public void clickPrint() {
			switchToIframe();
			shared_functions.getElementWhenClickable(By.cssSelector("#zoomPrintBtn-frame1Table")).click(); //Print	
		}
		public void clicDraftkPrint() {
			switchToIframe();
			shared_functions.getElementWhenClickable(By.cssSelector("#zoomDraftPrintBtn-frame1Table")).click(); //DraftPrint	
		}
		public void clickPreview() {
			switchToIframe();
			shared_functions.getElementWhenClickable(By.cssSelector("#zoomPreviewBtn-frame1Table")).click();	//Preview
			//try {WebElement eCloseInPreview = shared_functions.getElementWhenClickable(By.cssSelector("#dscnviewerPrintBtn+a"));}catch(Exception e){e.printStackTrace();}
		}
		public void clickPrintInPreview() {
			switchToIframe();
			shared_functions.getElementWhenClickable(By.cssSelector("#dscnviewerPrintBtn-frame1Table")).click(); //Print in Preview
		}
		public void clickCloseInPreview() {
			switchToIframe();
			WebDriverWait w = new WebDriverWait(driver, 30);
			w.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#pdfviewer_close"))).click();//Close in Preview
		}
		public void clickConsultationList() {
			switchToIframe();
			shared_functions.getElementWhenClickable(By.cssSelector("#zoomConsultListBtn-frame1Table")).click(); //Close in Preview
		}
		public void clickZoomInConsultationList() {
			switchToIframe();
			shared_functions.getElementWhenClickable(By.cssSelector("table#noteZoomBtn-frame1Table")).click(); //Zoom in Consultation List
		}
		
		public void clickTmpl() {
			switchToIframe();
			shared_functions.getElementWhenClickable(By.cssSelector("#zoomTmplBtn")).click(); //Tmpl
		}
	}
	class PAGE_NoteTemplate{
		WebDriver driver = null;
		public PAGE_NoteTemplate(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe_Left() {
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("320Panel");
			shared_functions.switchToFrameByString("tmplFrame");
			shared_functions.switchToFrameByString("noteTmplMain");
		}
		public void switchToIframe_Right() {
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("320Panel");
			shared_functions.switchToFrameByString("tmplFrame");
			shared_functions.switchToFrameByString("noteTmplDetail");
		}
		public void searchFor(String str) {
			switchToIframe_Left();
			WebElement eSearch = shared_functions.getElementWhenClickable(By.cssSelector("#searchTextPanel #searchText-bodyEl input"));
			eSearch.clear();
			eSearch.sendKeys( str );
			eSearch.sendKeys(Keys.ENTER);
		}
		public void waitUntilTemplateListFinishedLoading(){
			switchToIframe_Left();
			Boolean bLoading = true;
			while(bLoading){
				if(shared_functions.checkAndGetElementsWhenVisible(By.cssSelector("#tmplListGrid"))!=null){
					System.out.println("Template list is finished loading");
					bLoading=false;
				}else{
					System.out.println("Template list is loading");
				}
			}
		}
		public void selectTemplate(int idx) {
			switchToIframe_Left();
			if( shared_functions.checkAndGetElementsWhenVisible(By.cssSelector("#tmplListGrid #tmplListGrid-body table tr"))!=null ) {
				List<WebElement> liTemplate=shared_functions.getElementsWhenVisible(By.cssSelector("#tmplListGrid #tmplListGrid-body table tr"));
				WebElement eTemplate=shared_functions.getElementsInsideParentWebElementWhenVisible(liTemplate.get(idx),By.cssSelector("td")).get(0);
				eTemplate.click();
			}
			switchToIframe_Right();
			shared_functions.getElementWhenClickable(By.cssSelector("#selectBtn")).click();
		}
		public void inputTemplateContent(String str) {
			switchToIframe_Right();
			shared_functions.getElementWhenClickable(By.cssSelector("#tmplContent")).sendKeys( str ); 
		}
	}
	class PAGE_ConsultationList{
		WebDriver driver = null;
		String cssZoomText = "textarea#noteTxtAreaPlain-inputEl";
		public PAGE_ConsultationList(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void clickCaseNo(String caseNum) {
			String strXPathCaseNo = "//div[ contains(text(),'"+caseNum+"') ]";
			WebElement eXPathCaseNo = shared_functions.getElementWhenClickable(By.xpath( strXPathCaseNo ));
			eXPathCaseNo.click();
		}
		public Boolean verifyDesc() {
			String strXPathDesc = "//textarea[text()='This is the current record on the right-hand side!']";
			return shared_functions.checkAndGetElementsWhenVisible(By.xpath( strXPathDesc ))!=null;
		}
		public void clickOKBtn() {
			String strXPathOKBtn = "//span[contains(text(),'K')]//descendant::u[contains(text(),'O')]";
			Boolean isExistOkBtn = shared_functions.checkAndGetElementsWhenVisible(By.xpath( strXPathOKBtn ))!=null;
			if( isExistOkBtn ) {
				shared_functions.getElementsWhenVisible(By.xpath( strXPathOKBtn )).get(0).click();
			}
		}

	}
}
