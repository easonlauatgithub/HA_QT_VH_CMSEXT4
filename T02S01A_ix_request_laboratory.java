package suite;

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
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

public class T02S01A_ix_request_laboratory {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_lxRequest lx = null;
	int steps_passed = 0;
	int total_steps = 3;
	String gcr_case_no = null;
	String version = "web";
	String today_str = shared_functions.getDateInddMMyyyySlash();

	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T02S01A_ix_request_laboratory";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T02S01A_extjs4");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		lx = new PAGE_PatientDetailPage_lxRequest(driver);
		gcr_case_no = dict.get("case_no");
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		
		if( dict.get("order_type").equals("radiology") ) {
			shared_functions.reporter_ReportEvent("micFail", "QAG_failure_1", "This script not intended to execute radiology ix requests");
		}
		open_ix_request();
		test_create_laboratory_orders();
		test_repeat_order();
		test_remove_tests_from_order();
		cmsMainPage.fnNextPatient();
		shared_functions.countTestPassed(steps_passed, total_steps);
	}
	public void open_ix_request() throws InterruptedException{
		cmsMainPage.fnPMI();
		cmsMainPage.selectPatientByCaseNum(dict.get("case_no"));
		psf.closeExistingAlertReminderWindow();
		reopen_ix_request();
	}
	public void reopen_ix_request() throws InterruptedException{
		cmsMainPage.fnNextPatient();
		cmsMainPage.fnIxRequest();
		cmsMainPage.selectPatientByCaseNum(dict.get("case_no"));
	    //select_function_and_case_number "2.Investigation", "Ix Request", DataTable.GlobalSheet.GetParameter("case_no"), dict.item("specialty"), dict.item("subspecialty")
		//If Not ix_frame.Exist(1) Then
			//Reporter.ReportEvent micFail, "QAG_failure", "Unable to open the function"
			//close_all_functions()
			//ExitTestIteration
		//End If
		driver.switchTo().defaultContent();
		driver.switchTo().frame("276Panel");
		//Set ix_frame = Browser("title:=HA CMS.*").Page("title:= HA CMS.*").Frame("html tag:=IFRAME","title:=GCR")
	    //Set order_detail_elm = ix_frame.WebEdit("html tag:=TEXTAREA","html id:=gcrTextField_gcr_orderDetail_admitDxInput.*","visible:=True","index:=0")
	    //If trim(order_detail_elm.GetROProperty("value")) = "" Then
	        //order_detail_elm.Click
	        //Wait 1
	        //WshShell.SendKeys dict.item("current_dx")
	        //WshShell.SendKeys "{ENTER}"
	        //Wait 1
	    //End If
	}
	public void test_create_laboratory_orders() throws Exception {
		System.out.println("test_create_laboratory_orders() - START");
		if( dict.get("order_type").equals("laboratory") ) {
			if( !dict.get("request_by").equals("N") ) { //AHN001
				lx.requestBy(dict.get("request_by")); //AHN001
				test_create_lab_order_histopathology();
				test_create_lab_order_bloodbank();
				test_create_lab_order_biochemistry();
				test_create_lab_order_haematology();
				test_create_lab_order_microbiology();
				test_check_lab_order_ix_history();
			}
		}
		System.out.println("test_create_laboratory_orders() - END");
	}
	public void test_repeat_order() throws Exception {
		System.out.println("test_repeat_order() - START");
		if( driver.findElements(By.xpath("//table[contains(@id, 'gcrIxReqByHistoryTbl')]")).size()<=0 ){
			lx.clickTabHistory();
		}
		if( !dict.get("request_by").equals("N") ){
			if( driver.findElements(By.xpath("//input[contains(@id, 'textBox~~gcr_orderDetail_reqByDD')]")).size()>0 ){
				shared_functions.sleepForAWhile(1000);
				WebElement e = driver.findElement(By.xpath("//input[contains(@id, 'textBox~~gcr_orderDetail_reqByDD')]"));
				shared_functions.clearAndSend(e, dict.get("request_by"));
				shared_functions.sleepForAWhile(1000);
			}
		}
		String xp_today_date = "//td[contains(text(), '"+today_str+"')]";
		List<WebElement> today_records_elms = driver.findElements(By.xpath(xp_today_date));
		double rand = Math.random();
		int selected_record_index = (int) (today_records_elms.size() * rand);
		int number_of_orders_to_repeat = 1;
		String[] order_numbers = new String[number_of_orders_to_repeat];
		for(int i=0; i<number_of_orders_to_repeat; i++){
			WebElement r = today_records_elms.get(selected_record_index);
			System.out.println("text123: "+today_records_elms.get(selected_record_index).getText());
			order_numbers[i] = today_records_elms.get(selected_record_index).getText();
		}
		for(int j=0; j<number_of_orders_to_repeat; j++){
			String order_number = order_numbers[j];
			shared_functions.reporter_ReportEvent("micDone", "repeat order "+(j+1), order_number);
			lx.clickTabHistory();
			String xp1 = "//td[contains(text(), '"+order_number+"')]";
			List<WebElement> li1 = driver.findElements(By.xpath(xp1));
			System.out.println("li1.size(): "+li1.size());
			System.out.println("selected_record_index: "+selected_record_index);
			li1.get(selected_record_index).click();
			String xp2 = "//table[contains(@id, 'gcrIxRequestRepeatBtn_tbl')]";
			List<WebElement> li2 = driver.findElements(By.xpath(xp2));
			li2.get(0).click();
			
			String xp_check = "//span[contains(text(), 'New Request Information')]";
			List<WebElement> li_check = driver.findElements(By.xpath(xp_check));				
			if( li_check.get(0).isDisplayed() ){
				String xp = "//table[contains(@id, 'gcr_orderDetail_okButton_')]";
				List<WebElement> li = driver.findElements(By.xpath(xp));
				li.get(0).click();
			}
			funcClickOKForRepeatedItem();
			today_records_elms = driver.findElements(By.xpath(xp_today_date));
			shared_functions.do_screen_capture_with_filename(driver, "T02S01_2");
			if( today_records_elms.size() == (Integer.parseInt(dict.get("items_to_check")) + number_of_orders_to_repeat) ){
				steps_passed = steps_passed + 1;
				shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "repeat order - ["+number_of_orders_to_repeat+"] number of order repeated");
			}else{
				shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_2", "after order repeat, only "+today_records_elms.size()+" number of orders found, expected: " + (Integer.parseInt(dict.get("items_to_check"))+number_of_orders_to_repeat) );
			}
		}
		System.out.println("test_repeat_order() - END");
	}
	public void funcClickOKForRepeatedItem() throws InterruptedException{
		System.out.println("funcClickOK() - START");
		System.out.println("bloodbank specific");
        //bloodbank specific
		if( driver.findElements(By.xpath("//table[contains(@id, 'gcr_bloodBankDetailPanel_okButton_')]")).size()>0 ){
			driver.findElement(By.xpath("//table[contains(@id, 'gcr_bloodBankDetailPanel_okButton_')]")).click();
		}else if( driver.findElements(By.xpath("//table[contains(@id, 'gcr_bloodBankDetailPanel_okButton_undefined_tbl')]")).size()>0 ){
			driver.findElement(By.xpath("//table[contains(@id, 'gcr_bloodBankDetailPanel_okButton_undefined_tbl')]")).click();
		}
		System.out.println("specimen detail");
        //specimen detail
		if( driver.findElements(By.xpath("//table[contains(@id, 'gcrSpecDtlPopupOkBtn_tbl')]")).size()>0 ){
			driver.findElement(By.xpath("//table[contains(@id, 'gcrSpecDtlPopupOkBtn_tbl')]")).click();
		}
		System.out.println("Radiology orders");
        //Radiology orders
		if( driver.findElements(By.xpath("//table[contains(@id, 'gcrRad_POPUP_okBtn_tbl')]")).size()>0 ){
			driver.findElement(By.xpath("//table[contains(@id, 'gcrRad_POPUP_okBtn_tbl')]")).click();
		}
		if( driver.findElements(By.xpath("//table[contains(@id, 'gcrIxInformationEngineMainPanel-xfolder-edit-saveBtn_tbl')]")).size()>0 ){
			PAGE_BloodBank bb = new PAGE_BloodBank(driver);
			bb.auto_fillin_select_options("repeating_orders");
			driver.findElement(By.xpath("//table[contains(@id, 'gcrIxInformationEngineMainPanel-xfolder-edit-saveBtn_tbl')]")).click();
		}
		System.out.println("duplicate ix/service, request");
		//duplicate ix/service, request
		if( driver.findElements(By.xpath("//span[contains(text(), 'Duplicated Ix/Service')]")).size()>0 ){
			driver.findElement(By.xpath("//textarea[contains(@id, 'gcrTextField_gcrDupReason')]")).click();
			driver.findElement(By.xpath("//textarea[contains(@id, 'gcrTextField_gcrDupReason')]")).sendKeys("For QAG testing");
			driver.findElement(By.xpath("//a[contains(@id, 'maxDupOKBtn_btnTxt')]")).click();
		}
		System.out.println("gcr_orderCart_addIxSaveBtn_");
        //ix_frame.Link("html tag:=A","innertext:=Save","visible:=True").Click
		if( driver.findElements(By.xpath("//table[contains(@id, 'gcr_orderCart_addIxSaveBtn_')]")).size()>0 ){
			driver.findElement(By.xpath("//table[contains(@id, 'gcr_orderCart_addIxSaveBtn_')]")).click();			
		}
        //close any GCR-RIS popup
		if( driver.findElements(By.xpath("//span[contains(text(), 'GCR-RIS appointment booking')]")).size()>0 ){
			driver.findElement(By.xpath("//table[contains(text(), 'Skip')]")).click();
		}
        //cancel_printout_saveas_dialogue()
        //If Window("regexpwndtitle:=GCRS","regexpwndclass:=SunAwtDialog","index:=0").exist(5) Then
        //    Window("regexpwndtitle:=GCRS","regexpwndclass:=SunAwtDialog","index:=0").Close
        //End If
		wait_history_page_loading();
		System.out.println("funcClickOK() - END");
	}
	public void wait_history_page_loading() throws InterruptedException{
		shared_functions.sleepForAWhile(1000);
		long start = System.currentTimeMillis();
		long end = start+10000;
		while(System.currentTimeMillis() < end) {
			if(!driver.findElement(By.xpath("//span[contains(text(), 'Please Wait...')]")).isDisplayed()){
				break;
			}	
		}
		shared_functions.sleepForAWhile(1000);
	}
	public void test_remove_tests_from_order() throws Exception {
		System.out.println("test_remove_tests_from_order() - START");
		if( driver.findElements(By.xpath("//table[contains(@id, 'gcrIxReqByHistoryTbl')]")).size()<=0 ){
			lx.clickTabHistory();
		}
		String xp_today_date = "//td[contains(text(), '"+today_str+"')]";
		List<WebElement> today_records_elms = driver.findElements(By.xpath(xp_today_date));
		String[] order_numbers = new String[today_records_elms.size()];
		for(int i=0; i<today_records_elms.size(); i++){
			WebElement r = today_records_elms.get(i);
			order_numbers[i] = today_records_elms.get(i).getText();
		}
		for(int j=0; j<today_records_elms.size(); j++){
			String order_number = order_numbers[j];
			System.out.println("order_number: "+order_number);
			String xp_order_number = "//td[contains(text(), '"+order_number+"') and @class='scrollContentTableCell']";
			List<WebElement> li_order_number = driver.findElements(By.xpath(xp_order_number));
			System.out.println("li_order_number.size(): "+li_order_number.size());
			li_order_number.get(0).click();
			if( driver.findElements(By.xpath( "//b[contains(text(),'Remo')]//u[contains(text(),'v')]" )).size()>0 ){
				driver.findElement(By.xpath( "//b[contains(text(),'Remo')]//u[contains(text(),'v')]" )).click();				
			}else{
				System.out.println("else 1");
			}
			if( driver.findElements(By.xpath("//td[contains(text(), 'Yes')  and @class='x-btn-center-cui-n' ]")).size()>0 ){
				driver.findElement(By.xpath("//td[contains(text(), 'Yes')  and @class='x-btn-center-cui-n' ]")).click();
			}else{
				System.out.println("else 2");
			}
			if( driver.findElements(By.xpath("//span[contains(text(),'es') and @class='x-btn-inner x-btn-inner-center']//u[contains(text(),'Y')]")).size()>0 ){
				driver.findElement(By.xpath("//span[contains(text(),'es') and @class='x-btn-inner x-btn-inner-center']//u[contains(text(),'Y')]")).click();
			}else{
				System.out.println("else 3");
			}
			if( driver.findElements(By.xpath("//textarea[@id='gcrTextField_ixHistoryRemoveComment' ]")).size()>0 ){
				driver.findElement(By.xpath("//textarea[@id='gcrTextField_ixHistoryRemoveComment' ]")).click();
				driver.findElement(By.xpath("//textarea[@id='gcrTextField_ixHistoryRemoveComment' ]")).sendKeys("qag remove item testing");
				driver.findElement(By.xpath("//a[contains(text(), 'Save') and @id='gcrSaveRemoveBtn_btnTxt']")).click();
				shared_functions.sleepForAWhile(5000);
			}else{
				System.out.println("else 4");
			}
			wait_history_page_loading();
		}
		shared_functions.sleepForAWhile(5000);
		today_records_elms = driver.findElements(By.xpath(xp_today_date));
		shared_functions.do_screen_capture_with_filename(driver, "T02S01_3");
		if( today_records_elms.size() == 0 ){
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "remove order - no more today's order found");
		}	
		System.out.println("test_remove_tests_from_order() - END");		
	}
	public void test_create_lab_order_histopathology() throws InterruptedException, FindFailed {
		System.out.println("test_create_lab_order_histopathology() - START");
		lx.clickTabDiscipline();
		lx.selectDiscipline("A Histopathology");
		lx.clickInnerTab(dict.get("histo_tab")); //Histopathology
		lx.selectChkBox(dict.get("histo_site")); //Liver
		if(dict.get("histo_tab").equals("Histopathology")) {
			lx.selectRdbtn(dict.get("histo_option")); //Frozen Section
			lx.inputSpecifiedLocation(dict.get("histo_location")); //Left lobe
			lx.inputTypeOfOperation(dict.get("histo_operation"));  //Incision
			lx.inputPreparationRemark(dict.get("histo_preparation")); //fresh
		}
		lx.clickAddIxBtn();
		lx.clickSaveBtn();
		lx.handleGcrRisPopup();
		lx.HARDCODE_handlePrintingApplet();
		shared_functions.sleepForAWhile(5000);
		System.out.println("test_create_lab_order_histopathology() - END");
	}
	public void test_create_lab_order_bloodbank() throws InterruptedException {
		System.out.println("test_create_lab_order_bloodbank() - START");
		lx.clickTabDiscipline();
		lx.selectDiscipline("B Blood Bank");
		lx.selectRdbtn(dict.get("bloodbank_test"));
		lx.selectRdbtn(dict.get("bloodbank_urgency"));
		//Date Required
		List<WebElement> li = driver.findElements(By.xpath("//input[contains(@id,'gcrDateField_gcr_bloodBankDetailPanel_dateRequired')]"));
		if(li.size()>0) {
			WebElement e = li.get(0);
			shared_functions.clearAndSend(e,today_str);
		}
		//Request For
		String xp2 = "//span[contains(text(),'"+dict.get("bloodbank_requestfor")+"')]"; //Plasma
		System.out.println("xp2: "+xp2);
		List<WebElement> li2 = driver.findElements(By.xpath(xp2));
		System.out.println("li2.size(): "+li2.size());
		if(li2.size()>0) {
			WebElement e = li2.get(0);
			System.out.println("id: "+e.getAttribute("id"));
			e.click();
		}
		//3 units
		String e2ID = li2.get(0).getAttribute("id");
		String request_for_htmlid_suffix = e2ID.substring(e2ID.length()-10, e2ID.length());
		String id3 = "gcrTextField_gcr_bloodBankDetailPanel_requestUnit_DIV__" + request_for_htmlid_suffix;
		System.out.println("id3: "+id3);
		String xp3 = "//*[contains(@id,'"+id3+"')]";
		List<WebElement> li3 = driver.findElements(By.xpath(xp3));
		System.out.println("li3.size(): "+li3.size());
		if(li3.size()>0) {
			WebElement e = li3.get(0);
			shared_functions.clearAndSend(e, dict.get("bloodbank_requestunit")); //3
			e.click();
		}
		//Irradiated
		String xp4 = "//span[contains(text(),'"+dict.get("bloodbank_special_req")+"')]"; //Irradiated
		List<WebElement> li4 = driver.findElements(By.xpath(xp4));
		System.out.println("li4.size(): "+li4.size());
		if(li4.size()>0) {
			WebElement e = li4.get(0); 
			e.click();
		}
		//Add Ix
		lx.clickAddIxBtn();
		PAGE_BloodBank bb = new PAGE_BloodBank(driver);
		bb.auto_fillin_select_options("bloodbank");
		bb.save();
		lx.clickSaveBtn();
		lx.handleGcrRisPopup();
		System.out.println("test_create_lab_order_bloodbank() - END");
	}
	public void test_create_lab_order_biochemistry() throws InterruptedException {
		System.out.println("test_create_lab_order_biochemistry() - START");
		lx.clickTabDiscipline();
		lx.selectDiscipline("C Biochemistry");
		lx.selectChkBox(dict.get("biochemistry_item"));
		lx.clickAddIxBtn();
		lx.clickSaveBtn();
		lx.handleGcrRisPopup();
		System.out.println("test_create_lab_order_biochemistry() - END");
	}
	public void test_create_lab_order_haematology() throws InterruptedException {
		System.out.println("test_create_lab_order_haematology() - START");
		lx.clickTabDiscipline();
		lx.selectDiscipline("H Haematology");
		lx.selectChkBox(dict.get("haematology_item"));
		lx.clickAddIxBtn();
		lx.clickSaveBtn();
		lx.handleGcrRisPopup();
		System.out.println("test_create_lab_order_haematology() - END");
	}
	public void test_create_lab_order_microbiology() throws InterruptedException {
		System.out.println("test_create_lab_order_microbiology() - START");
		lx.clickTabDiscipline();
		lx.selectDiscipline("M Microbiology");
		lx.selectRdbtn(dict.get("microbiology_specimen"));
		lx.selectChkBox(dict.get("microbiology_test"));
		lx.clickAddIxBtn();
		lx.clickSaveBtn();
		lx.handleGcrRisPopup();
		System.out.println("test_create_lab_order_microbiology() - END");
	}
	public void test_check_lab_order_ix_history() throws Exception {
		System.out.println("test_check_lab_order_ix_history() - START");
		lx.clickTabHistory();
		shared_functions.do_screen_capture_with_filename(driver,"T02S01_1");
		String check_histo_text = dict.get("histo_option") + ", " + dict.get("histo_site");
		String check_bloodbank_text = dict.get("bloodbank_test");
		String check_biochemistry_text = dict.get("biochemistry_item");
		String check_haematology_text = dict.get("haematology_item");
		String check_microbiology_text = dict.get("microbiology_test");
		Boolean b = lx.check_history_page_item(check_histo_text)&&lx.check_history_page_item(check_bloodbank_text)
				&&lx.check_history_page_item(check_biochemistry_text)&&lx.check_history_page_item(check_haematology_text)
				&&lx.check_history_page_item(check_microbiology_text); 
		if(b) {
			steps_passed = steps_passed + 1;
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1a", "5 Laborartory order items matched in history page");
		}
		System.out.println("test_check_lab_order_ix_history() - END");
	}
	
	class PAGE_PatientDetailPage_lxRequest{
		WebDriver driver = null;
		public PAGE_PatientDetailPage_lxRequest(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIXframe() {
			driver.switchTo().defaultContent();
			driver.switchTo().frame("276Panel");			
		}
		public void requestBy(String str) throws InterruptedException { 
			List<WebElement> liRequestBy = driver.findElements(By.xpath("//input[starts-with(@id, 'textBox~~gcr_orderDetail_reqByDD')]"));
			if(liRequestBy.size()>0) {
				WebElement eRequestBy = liRequestBy.get(0);
				while( !(eRequestBy.isEnabled()&&eRequestBy.isDisplayed()) ){
					System.out.println("requestBy - eRequestBy not enabled, not displayed, continue to loop");
				}
				System.out.println("eRequestBy.isEnabled():"+eRequestBy.isEnabled());
				System.out.println("eRequestBy.isDisplayed():"+eRequestBy.isDisplayed());
				eRequestBy.click();
				shared_functions.sleepForAWhile(1000);
				shared_functions.clearAndSend(eRequestBy, str);
			}			
		}
		public void clickTabDiscipline() {
			List<WebElement> liDiscipline = driver.findElements(By.cssSelector("#gcrIxByDisciplineRightClick"));
			if(liDiscipline.size()>0) {
				WebElement eDiscipline = liDiscipline.get(0);
				eDiscipline.click();
			}
		}
		public void clickTabHistory() {
			List<WebElement> liHistory = driver.findElements(By.cssSelector("#gcrIxByHistoryRightClick"));
			if(liHistory.size()>0) {
				WebElement eHistory = liHistory.get(0);
				eHistory.click();
			}
		}
		public void selectDiscipline(String str) throws InterruptedException {
			List<WebElement> li = driver.findElements(By.xpath("//td[contains(text(),'"+str+"')]"));
			if(li.size()>0) {
				WebElement e = li.get(0);
				e.click();
			}
			shared_functions.sleepForAWhile(1000);
		}
		public void clickInnerTab(String str) { 
			List<WebElement> li = driver.findElements(By.xpath("//span[contains(text(),'"+str+"')]"));
			if(li.size()>0) {
				WebElement e = li.get(0);
				e.click();
			}			
		}
		public void selectChkBox(String str) { 
			List<WebElement> li = driver.findElements(By.xpath("//span[contains(text(),'"+str+"')]"));
			if(li.size()>0) {
				WebElement e = li.get(0);
				e.click();
			}			
		}
		public void selectRdbtn(String str) {
			List<WebElement> li = driver.findElements(By.xpath("//span[contains(text(),'"+str+"')]"));
			if(li.size()>0) {
				WebElement e = li.get(0);
				System.out.println("selectRdbtn("+str+") e.isDisplayed()"+ e.isDisplayed());
				e.click();
			}			
		}
		public void inputSpecifiedLocation(String str){ 
			List<WebElement> liSpecifiedLocation = driver.findElements(By.xpath("//input[contains(@id, 'textBox~~gcrHistoSpecifyLoc_HS_Div')]"));
			if(liSpecifiedLocation.size()>0) {
				WebElement eSpecifiedLocation = liSpecifiedLocation.get(0);
				shared_functions.clearAndSend(eSpecifiedLocation, str);
			}
		}
		public void inputTypeOfOperation(String str){
			List<WebElement> liTypeOfOperation = driver.findElements(By.xpath("//input[contains(@id, 'textBox~~gcrHistoTypeOfOp_HS_Div')]"));
			System.out.println("liTypeOfOperation: "+liTypeOfOperation.size());
			if(liTypeOfOperation.size()>0) {
				WebElement eTypeOfOperation = liTypeOfOperation.get(0);
				shared_functions.clearAndSend(eTypeOfOperation, str);
			}			
		}
		public void inputPreparationRemark(String str){
			List<WebElement> liPreparationRemark = driver.findElements(By.xpath("//input[contains(@id, 'textBox~~gcrHistoPrepare_HS_Div')]"));
			System.out.println("liPreparationRemark: "+liPreparationRemark.size());
			if(liPreparationRemark.size()>0) {
				WebElement ePreparationRemark = liPreparationRemark.get(0);
				shared_functions.clearAndSend(ePreparationRemark, str);
			}			
		}
		public void clickAddIxBtn() throws InterruptedException {
			List<WebElement> liAddIxBtn = driver.findElements(By.xpath("//table[contains(@id, 'gcr_newIx_disc_addIxBtn_')]"));
			System.out.println("liAddIxBtn: "+liAddIxBtn.size());
			if(liAddIxBtn.size()>0) {
				WebElement eAddIxBtn = liAddIxBtn.get(0);
				eAddIxBtn.click();
			}
			shared_functions.sleepForAWhile(3000);
		}
		public void clickSaveBtn() throws InterruptedException {
			List<WebElement> liIxSaveBtn = driver.findElements(By.xpath("//table[contains(@id, 'gcr_orderCart_addIxSaveBtn_')]"));
			System.out.println("liIxSaveBtn: "+liIxSaveBtn.size());
			if(liIxSaveBtn.size()>0) {
				WebElement eIxSaveBtn = liIxSaveBtn.get(0);
				eIxSaveBtn.click();
			}
			shared_functions.sleepForAWhile(3000);
		}
		public void handleGcrRisPopup() {
			List<WebElement> liGcrRisPopUp = driver.findElements(By.xpath("//span[contains(text(), 'GCR-RIS appointment booking')]"));
			System.out.println("liGcrRisPopUp: "+liGcrRisPopUp.size());
			if(liGcrRisPopUp.size()>0) {
				WebElement eSkip = driver.findElement(By.xpath("//span[contains(text(), 'Skip')]"));
				eSkip.click();
			}			
		}
		public Boolean check_history_page_item(String check_text) {
			Boolean item_found = false;
			String xPath = "//td[contains(text(), '"+today_str+"')]//following-sibling::td";
			List<WebElement>li = driver.findElements(By.xpath(xPath));
			for(int i=0; i<li.size(); i++){
				String innerText = li.get(i).getText();
				if(innerText.equals(check_text)){
					item_found=true;
				}
			}			
			return item_found;
		}
		public void HARDCODE_handlePrintingApplet() throws FindFailed, InterruptedException {
			String screenCapturePath = "C:\\eclipse_neon\\workspace\\TestSelenium\\screencapture_applet\\";
			Screen screen = new Screen(0);
			Screen screen1 = new Screen(1);
			if ( screen1.exists(screenCapturePath+"GcrPrintEngineApplet.PNG") != null ){
				System.out.println("screen1");
				screen1.click(screenCapturePath+"applet_checkbox.PNG");
				screen1.click(screenCapturePath+"applet_confirm.PNG");
			}else {
				System.out.println("not in screen1");
			}
			if ( screen.exists(screenCapturePath+"GcrPrintEngineApplet.PNG") != null ){
				System.out.println("screen");
				screen.click(screenCapturePath+"applet_checkbox.PNG");
				screen.click(screenCapturePath+"applet_confirm.PNG");
			}else {
				System.out.println("not in screen");
			}
			shared_functions.sleepForAWhile(3000);
		}
	}
	class PAGE_BloodBank{
		WebDriver driver = null;
		public PAGE_BloodBank(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void auto_fillin_select_options(String fillin_phrase) {
			auto_fill_dropdown();
			auto_fill_radiobutton();
			auto_fill_textarea();
		}
		public void auto_fill_dropdown() {
			String xpDropDown = "//input[contains(@id,'textBox~~gcrInputId_')]";
			List<WebElement> liDropDown = driver.findElements(By.xpath(xpDropDown));
			for(int i=0; i<liDropDown.size(); i++){
				if(liDropDown.get(i).getAttribute("value").isEmpty()){
					liDropDown.get(i).click();
					liDropDown.get(i).sendKeys(Keys.DOWN);
					liDropDown.get(i).sendKeys(Keys.DOWN);
					liDropDown.get(i).sendKeys(Keys.TAB);
				}
			}
		}
		public void auto_fill_radiobutton() {
			String xpRadioBtn = "//input[contains(@name,'gcrInputId_')]";
			List<WebElement> liRadioBtn = driver.findElements(By.xpath(xpRadioBtn));
			liRadioBtn.get(1).click();
		}
		public void auto_fill_textarea() {
			String xpTextArea = "//input[contains(@id,'gcrTextField_gcrInputId_')]";
			List<WebElement> liTextArea = driver.findElements(By.xpath(xpTextArea));
			System.out.println("liTextArea.size(): "+liTextArea.size());
			for(int k=0; k<liTextArea.size(); k++){
				System.out.println("liTextArea value: "+liTextArea.get(k).getAttribute("value"));
				System.out.println("liTextArea isEmpty: "+liTextArea.get(k).getAttribute("value").isEmpty());
				if(liTextArea.get(k).getAttribute("value").isEmpty()){
					liTextArea.get(k).click();
					liTextArea.get(k).sendKeys("QAG_gcr "+k);
				}
			}
		}
		public void save() {
			String css12 = "#gcrIxInformationEngineMainPanel-xfolder-edit-saveBtn_btnTxt";
			List<WebElement> li12 = driver.findElements(By.cssSelector(css12));
			System.out.println("li12.size() Save: "+li12.size());
			if(li12.size()>0) {
				WebElement e = li12.get(0);
				e.click();
			}			
		}
	}
}
