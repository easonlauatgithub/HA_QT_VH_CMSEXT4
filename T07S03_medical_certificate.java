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


public class T07S03_medical_certificate {
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	int steps_passed ;
	int total_steps ; 
	Map<String, String> dict = new HashMap<>();
	PAGE_PatientDetailPage_LetterDocument ld = null;
	PAGE_PatientDetailPage_MedicalCertHA37 mc = null;
	
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T07S03_medical_certificate";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T07S03");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		ld = new PAGE_PatientDetailPage_LetterDocument(driver);
		mc = new PAGE_PatientDetailPage_MedicalCertHA37(driver);
		steps_passed = 0;
		total_steps = 5;
		//driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		open_medical_cert();
		create_medical_cert();
		update_medical_cert();
		copy_medical_cert();
		delete_medical_cert();
		shared_functions.countTestPassed(steps_passed, total_steps);
		cmsMainPage.fnNextPatient();
	}
	public void open_medical_cert() {
		System.out.println("open_medical_cert() - START");
		cmsMainPage.fnLetterDocument();
		cmsMainPage.selectPatientByCaseNum(dict.get("case_no"));	
		System.out.println("open_medical_cert() - END");
	}
	public void create_medical_cert() throws Exception {
		System.out.println("create_medical_cert() - START");
		ld.selectMedicalCert();
		ld.clickOKBtn();
		mc.inputSufferFrom(dict.get("suffer_from"));
		//Leave period
		//main_frame.WebEdit("html tag:=INPUT", "html id:=txt_leave_period", "visible:=True").Click
		//main_frame.WebElement("class:=x-btn-inner x-btn-inner-center", "innertext:=\+", "html tag:=SPAN").Click
		//Wait 1
		mc.inputRemarks(dict.get("create_mle_remarks"));
		mc.inputFor(dict.get("ddlb3_med_cert_for"));
		mc.clickSaveNPrintBtn();
		ld.selectMedicalCert();
		ld.clickOKBtn();
		mc.clickHistoryTab();
		mc.selectFirstRecord();
		shared_functions.do_screen_capture_with_filename(driver, "T07S03_1");
		String actualRemarks = mc.getActualRemarks();
		String expectedRemarks = dict.get("create_mle_remarks");
		if(actualRemarks.equals(expectedRemarks)){
			steps_passed = steps_passed + 1;
			System.out.println("create_medical_cert() - verify remarks created");
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "create medical cert - [today] created record found, remarks field match");
		}
		mc.clickCancelBtn();
		System.out.println("create_medical_cert() - END");
	}
	public void update_medical_cert() throws Exception {
		System.out.println("update_medical_cert() - START");
		ld.selectMedicalCert();
		ld.clickOKBtn();
		mc.clickHistoryTab();
		mc.selectFirstRecord();
		mc.clickAmendBtn();
		mc.inputRemarks("");
		mc.selectTemplate("Follow Up");
		mc.clickPasteBtn();
		mc.clickSaveBtn();
		mc.clickHistoryTab();
		mc.selectFirstRecord();
		shared_functions.do_screen_capture_with_filename(driver, "T07S03_2");
		String actualRemarks = mc.getActualRemarks().replace("\n", "").replace("\r", "");
		String expectedRemarks = dict.get("update_mle_remarks").replace("\\", "");
		if(actualRemarks.equals(expectedRemarks)){
			System.out.println("update_medical_cert() - verify remarks updated");
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "update medical certificate - remarks field is updated");
		}
		mc.clickCancelBtn();
		System.out.println("update_medical_cert() - END");
	}
	public void copy_medical_cert() throws Exception {
		System.out.println("copy_medical_cert() - START");
		ld.selectMedicalCert();
		ld.clickOKBtn();
		mc.clickHistoryTab();
		mc.selectFirstRecord();
		mc.clickCopyBtn();
		//	'Leave period
		//main_Frame.WebElement("class:=x-btn-inner x-btn-inner-center", "innertext:=\+", "html tag:=SPAN").Click
		//Wait 2
		mc.inputRemarks(dict.get("copy_mle_remarks"));
		mc.inputFor(dict.get("ddlb3_med_cert_for"));
		mc.clickSaveBtn();
		mc.clickHistoryTab();
		mc.selectFirstRecord();
		shared_functions.do_screen_capture_with_filename(driver, "T07S03_3");
		String actualRemarks = mc.getActualRemarks();
		String expectedRemarks = dict.get("copy_mle_remarks");
		if(actualRemarks.equals(expectedRemarks)){
			steps_passed = steps_passed + 1;
			System.out.println("copy_medical_cert() - verify remarks copied");
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "copy to new medical certificate - remarks field is copied");
		}
		mc.clickCancelBtn();
		System.out.println("copy_medical_cert() - END");
	}
	public void delete_medical_cert() throws Exception{
		System.out.println("delete_medical_cert() - START");
		delete_medical_cert(dict.get("copy_mle_remarks"),"copy to new","4");
		delete_medical_cert(dict.get("update_mle_remarks"),"update","5");
		System.out.println("delete_medical_cert() - END");
	}
	public void delete_medical_cert(String check_remarks, String t, String checkpoint_number) throws Exception {
		ld.selectMedicalCert();
		ld.clickOKBtn();
		mc.clickHistoryTab();
		mc.selectFirstRecord();
		mc.clickDeleteBtn();
		mc.confirmDeleteBtn();
		
		ld.selectMedicalCert();
		ld.clickOKBtn();
		mc.clickHistoryTab();
		mc.selectFirstRecord();
		Boolean record_found = mc.isExistRecord(check_remarks);
		shared_functions.do_screen_capture_with_filename(driver, "T07S03_"+checkpoint_number);
		if(!record_found){
			steps_passed = steps_passed + 1;
			System.out.println("delete_medical_cert() - verify remarks deleted "+check_remarks);
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_"+checkpoint_number, "delete medical certificate - '"+t+"' medical certificate is deleted");
		}
		mc.clickCancelBtn();
	}
	
	class PAGE_PatientDetailPage_LetterDocument{
		WebDriver driver = null;
		public PAGE_PatientDetailPage_LetterDocument(WebDriver driver){
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe(){
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("353Panel");
		}
		public void selectMedicalCert(){
			switchToIframe();
			String xp = "//label[contains(text(),'edical Certificate ( Sick Leave Form HA37 )')]//u[contains(text(),'M')]";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
		}
		public void clickOKBtn(){
			switchToIframe();
			String xp = "//span[@id='f9menu_btn_ok-btnInnerEl']";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
		}		
	}
	class PAGE_PatientDetailPage_MedicalCertHA37{
		WebDriver driver = null;
		public PAGE_PatientDetailPage_MedicalCertHA37(WebDriver driver){
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe(){
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("353Panel");
			shared_functions.switchToFrameByString("ifLetterDiv");
		}
		public void switchToIframeHistoryDetail(){
			switchToIframe();
			shared_functions.switchToFrameByString("ifLetterHist");
		}
		//New
		public void inputSufferFrom(String str){
			switchToIframe();
			String xp = "//textarea[@id='mle_suffer']";
			WebElement e = shared_functions.getElementWhenClickable(By.xpath(xp));
			e.click();
			e.sendKeys(str);
		}
		public void inputRemarks(String str){
			String xp = "//textarea[@id='mle_remarks']";
			WebElement e = shared_functions.getElementWhenClickable(By.xpath(xp));
			e.click();
			e.clear();
			e.sendKeys(str);
		}
		public void inputFor(String str){
			//String xp = "//img[@id='imgddlb3_med_cert_for']";
			String xp = "//input[@id='ddlb3_med_cert_for']";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
			String xp2 = "//td[contains(text(),'"+str+"')]";
			shared_functions.clickElementWhenClickable(By.xpath(xp2));
		}
		public void clickSaveNPrintBtn() throws InterruptedException{
			String xp = "//span[@id='btnSaveAndPrint-btnInnerEl']";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
			waitForSaving();
		}
		public void clickCopyBtn() throws InterruptedException{
			String xp = "//span[@id='btnHistCopyToNew-btnInnerEl']";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
			waitForSaving();
		}
		//History
		public void clickHistoryTab() throws InterruptedException{
			switchToIframe();
			String xp = "//span[contains(text(),'History')]";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
			waitForSaving();
		}
		public void selectFirstRecord(){
			System.out.println("selectFirstRecord");
			String xp = "//td[contains(@class,'x-grid-rowwrap')]//table[contains(@class,'x-gridview-')]";			
			shared_functions.clickElementWhenClickable(By.xpath(xp));
		}
		public void clickCancelBtn() throws InterruptedException{
			switchToIframe();
			String xp = "//span[@id='btnHistClose-btnInnerEl']";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
			waitForSaving();
		}
		public void clickAmendBtn() throws InterruptedException{
			String xp = "//span[contains(text(),'mend')]//u[contains(text(),'A')]";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
			waitForSaving();
		}
		public void clickDeleteBtn(){
			switchToIframe();
			String xp = "//span[@id='btnHistDelete-btnInnerEl']";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
		}
		public void confirmDeleteBtn() throws InterruptedException{
			String xp = "//span[contains(text(),'es')]//u[contains(text(),'Y')]";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
			String xp2 = "//span[contains(text(),'K')]//u[contains(text(),'O')]";
			shared_functions.clickElementWhenClickable(By.xpath(xp2));
			waitForSaving();
		}
		public Boolean isExistRecord(String str){
			Boolean isExist = false;
			String xp = "//table[contains(text(),'"+str+"')]";
			List<WebElement> li = shared_functions.checkAndGetElementsWhenVisible(By.xpath(xp));
			if(li!=null){
				isExist = true;
			}else{
				isExist = false;
			}
			return isExist;
		}
		public String getActualRemarks(){
			switchToIframeHistoryDetail();
			String xp = "//textarea[@id='mle_remarks']";
			WebElement e = shared_functions.getElementWhenVisible(By.xpath(xp));
			String str = e.getText();
			return str;
		}
		//Edit
		public void selectTemplate(String str){
			String xp = "//input[@name='template_code']";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
			String xp2 = "//li[contains(text(),'"+str+"')]";
			shared_functions.clickElementWhenClickable(By.xpath(xp2));
		}
		public void clickPasteBtn(){
			String xp = "//span[@id='btnPaste-btnInnerEl']";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
		}
		public void clickSaveBtn() throws InterruptedException{
			String xp = "//span[@id='btnSave-btnInnerEl']";
			shared_functions.clickElementWhenClickable(By.xpath(xp));
			waitForSaving();
		}
		public void waitForSaving() throws InterruptedException{
			System.out.println("Do not sleep 3s now ...");
			//shared_functions.sleepForAWhile(3000);
		}
	}
	
	
	

}
