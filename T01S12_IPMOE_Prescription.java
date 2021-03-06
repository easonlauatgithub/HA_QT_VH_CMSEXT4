package suite;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class T01S12_IPMOE_Prescription {
	WebDriver driver = null;
	PAGE_CMSMainPage cmsMainPage = null;
	PAGE_PatientDetailPage_PatientSpecificFunction psf = null;
	PAGE_PatientDetailPage_IPMOEPrescription ip = null;
	String version =null;
	String ipmoe_ward =null;
	String case_no=null;
	int test_drug_num;
	String today_ddMMMyyyy =null;
	String today_ddMMM =null;
	Date one_hour_later=null;
	String one_hour_laterhhmm=null;
	int steps_passed ;
	int total_steps ; 
	Map<String, String> dict = new HashMap<>();
	
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T01S12_IPMOE_Prescription";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "T01S12_extjs4");
		dict = shared_functions.sam_gor();
		driver = shared_functions.driver;
		cmsMainPage = new PAGE_CMSMainPage(driver);
		psf = new PAGE_PatientDetailPage_PatientSpecificFunction(driver);
		ip = new PAGE_PatientDetailPage_IPMOEPrescription(driver);
		steps_passed = 0;
		total_steps = 3;
		version = "web";
		ipmoe_ward = dict.get("ward");
		case_no = dict.get("case_no");
		test_drug_num = -1;
		today_ddMMMyyyy = shared_functions.getDateIn("dd/MMM/yyyy"); //today_in_ddMMMyyyy("-")
		today_ddMMM = today_ddMMMyyyy.substring(0, 6);
		Date now = new Date(); 
		one_hour_later = shared_functions.addExtraMinute(now, 60);
		one_hour_laterhhmm=shared_functions.getHHMM(one_hour_later);
		System.out.println("today_ddMMMyyyy:"+today_ddMMMyyyy);
		System.out.println("today_ddMMM:"+today_ddMMM);
		System.out.println("one_hour_later:"+one_hour_later);
		System.out.println("one_hour_laterhhmm:"+one_hour_laterhhmm);
		//driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		//openDrugAdminByPatient();
		open_ipmoe_function();
		test_add_drug_item();
		test_modify_drug_item();
		test_remove_drug_item();
		shared_functions.countTestPassed(steps_passed, total_steps);
		cmsMainPage.fnNextPatient();
	}
	public void openDrugAdminByPatient() throws Exception {
		System.out.println("openDrugAdminByPatient - START");
		cmsMainPage.fnDrugAdminByPatient();
		cmsMainPage.selectPatientByCaseNum("HN08000026Z");
		driver.switchTo().defaultContent();
		shared_functions.switchToFrameByString("431Panel");
		String str = "Acknowledge";
		String xp = "//span[contains(text(),'"+str+"')]";
		List<WebElement> li = shared_functions.getElementsWhenVisible(By.xpath(xp));
		if(li!=null){
			li.get(0).click();
		}
		ip.waitToLoad();
		cmsMainPage.fnClose();
		System.out.println("openDrugAdminByPatient - END");
	}
	public void open_ipmoe_function() throws Exception {
		System.out.println("open_ipmoe_function - START");
		//cmsMainPage.changeWardForNormalPatientList(ipmoe_ward);
		cmsMainPage.fnIpPrescribing();
		cmsMainPage.selectPatientByCaseNum(case_no);
		psf.closeExistingAlertReminderWindow();
		System.out.println("ip.isExistIpPrescribePage()"+ip.isExistIpPrescribePage());
		if(!ip.isExistIpPrescribePage()){
			shared_functions.do_screen_capture_with_filename(driver, "T01S12 unable to open function");
			shared_functions.reporter_ReportEvent("micFail", "QAG_failure", "Unable to open the function");
			cmsMainPage.fnNextPatient();
			System.out.println("driver.quit()");
			shared_functions.exit_test_iteration();
		}
		System.out.println("ip.isExistFailToRetrievePMSInfo()"+ip.isExistFailToRetrievePMSInfo());
		if(ip.isExistFailToRetrievePMSInfo()){
			shared_functions.do_screen_capture_with_filename(driver, "T01S12 PMS not available");
			shared_functions.reporter_ReportEvent("micFail", "QAG_failure", "Unable to retrieve PMS information");
			cmsMainPage.fnNextPatient();
			shared_functions.exit_test_iteration();
		}
		System.out.println("ip.isExistPharmacyInfoOfSpecialty()"+ip.isExistPharmacyInfoOfSpecialty());
		if(ip.isExistPharmacyInfoOfSpecialty()){
			shared_functions.do_screen_capture_with_filename(driver, "T01S12 unable to open function");
			shared_functions.reporter_ReportEvent("micFail", "QAG_failure", "Unable to Pharmacy information is not defined");
			cmsMainPage.fnNextPatient();
			shared_functions.exit_test_iteration();
		}
		System.out.println("ip.isExistFailToRetrieveCMSInfo()"+ip.isExistFailToRetrieveCMSInfo());
		if(ip.isExistFailToRetrieveCMSInfo()){
			shared_functions.do_screen_capture_with_filename(driver, "T01S12 CMS not available");
			shared_functions.reporter_ReportEvent("micFail", "QAG_failure", "Unable to retrieve CMS information");
			cmsMainPage.fnNextPatient();
			shared_functions.exit_test_iteration();
		}
		System.out.println("ip.isExistNowLoading()"+ip.isExistNowLoading());
		if(ip.isExistNowLoading()){
			ip.waitToLoad();
			if(ip.isExistNowLoading()){
				shared_functions.reporter_ReportEvent("micFail", "QAG_failure", "the [Now loading ...] dialogue still exists, after 60seconds");
				cmsMainPage.fnNextPatient();
				shared_functions.exit_test_iteration();				
			}
		}
		System.out.println("open_ipmoe_function - END");
	}
	public void test_add_drug_item() throws Exception {
		System.out.println("test_add_drug_item - START"); 
		System.out.println("ip.isExistPreviousRx()"+ip.isExistPreviousRx());
		if(ip.isExistPreviousRx()){
			//ip_prescribe_page.WebTable("html tag:=TABLE","column names:=;Drug Search;","visible:=True").Click
		}
		int number_of_drugs = Integer.parseInt(dict.get("new_ipmoe_drugs_num")); //3
		int start_num = 1;
		if(test_drug_num > 0){
			start_num = test_drug_num;
			number_of_drugs = test_drug_num;			
		}
		for(int i=start_num; i<=number_of_drugs ; i++){
			String suffix = "_d" + i;
			System.out.println("test_add_drug_item,suffix:"+suffix+",drugtype:"+dict.get("new_ipmoe_drugtype"+suffix)+",dosage:"+dict.get("new_ipmoe_common_dosage"+suffix));
			String new_ipmoe_keyword = dict.get("new_ipmoe_keyword" + suffix);
			ip.inputDrugName(new_ipmoe_keyword);
			if( new_ipmoe_keyword.equals("AUGMENT") ){
				ip.selectByTagNText("span","Augmentin");
			}
			
			ip.selectByTagNText("span",dict.get("new_ipmoe_drugtype"+suffix));
			
			if(dict.get("new_ipmoe_common_dosage"+suffix)!=null){
				ip.selectByTagNText("span",dict.get("new_ipmoe_common_dosage"+suffix));
				ip.clickAddToMarBtn();
			}else{
				ip.clickEditBtn();
				//String xp = "//div[contains(@id,'oral-orderDetailRowCt-')]//div[contains(@class,'ipmoe ipmoe-ipEditWin-dosageRow')]//table[contains(@class,'x-table-layout')]//input";
				String xp = "//div[contains(@class,'ipmoe-ipEditWin-dosageRow')]//input";
				List<WebElement> li = shared_functions.getElementsWhenPresent(By.xpath(xp));
				ip.inputEditDrugFields(li, dict.get("new_ipmoe_dosage" + suffix), dict.get("new_ipmoe_frequency" + suffix), dict.get("new_ipmoe_prn"+suffix), 
						dict.get("new_ipmoe_route_site" + suffix), dict.get("new_ipmoe_start_date"+suffix), dict.get("new_ipmoe_start_time"+suffix),
						dict.get("new_ipmoe_end_date"+suffix), dict.get("new_ipmoe_end_time"+suffix), dict.get("new_ipmoe_duration"+suffix),
						dict.get("new_ipmoe_dose"+suffix));					
				dict.put("mar_panel_description"+suffix, ip.get_new_drug_panel_description());
				ip.clickAddToMarBtnAfterEdit();
			}
		}
		int drugs_found_in_mar = 0;
		for(int i=start_num; i<=number_of_drugs; i++){
			String suffix = "_d" + i;
			String drug_check_str = dict.get("new_ipmoe_mar_check" + suffix);
			if(ip.select_today_drug_in_mar(drug_check_str)!=null){
				drugs_found_in_mar=drugs_found_in_mar+1;
			}
		}
		shared_functions.do_screen_capture_with_filename(driver, "T01S12_1");
		if(drugs_found_in_mar == number_of_drugs){
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_1", "new ipmoe order - " + drugs_found_in_mar + " of drugs matched after added in MAR");
			steps_passed = steps_passed + 1;
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_1", "new ipmoe order - only " + drugs_found_in_mar + "/" + number_of_drugs + " of items match");
		}
		System.out.println("test_add_drug_item - END");
	}
	public void test_modify_drug_item() throws Exception{
		System.out.println("test_modify_drug_item - START");
		WebElement drug_to_modify = ip.select_today_drug_in_mar(dict.get("new_ipmoe_mar_check_d" + dict.get("modify_ipmoe_drug_index")));
		shared_functions.right_click(drug_to_modify, 10, 0);
		ip.selectByTagNText("span", "Modify");
		String xp = "//div[contains(@id,'oral-orderDetailRowCt-')]//div[contains(@class,'ipmoe ipmoe-ipEditWin-dosageRow')]//table[contains(@class,'x-table-layout')]//input";
		List<WebElement> li = shared_functions.getElementsWhenPresent(By.xpath(xp));
		ip.inputModifyDrugFields(li, dict.get("modify_ipmoe_dosage"), dict.get("modify_ipmoe_frequency"), dict.get("modify_ipmoe_prn"), 
				dict.get("modify_ipmoe_route_site"), dict.get("modify_ipmoe_start_date"), dict.get("modify_ipmoe_start_time"),
				dict.get("modify_ipmoe_end_date"), dict.get("modify_ipmoe_end_time"), dict.get("modify_ipmoe_duration"),dict.get("modify_ipmoe_dose"));
		dict.put("modified_mar_panel_description", ip.get_new_drug_panel_description());
		ip.selectByTagNText("span", "Accept Change");
		shared_functions.do_screen_capture_with_filename(driver, "T01S12_2");
		if(ip.select_today_drug_in_mar(dict.get("modify_ipmoe_mar_check"))!=null){ 
			//Paracetamol Alcohol Free suspensionoral: 500 mg Q6H PRN for 1 day(s)
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_2", "modify ipmoe drug, modified item matched expected");
			steps_passed = steps_passed + 1;
			dict.put("new_ipmoe_mar_check_d" + dict.get("modify_ipmoe_drug_index"), dict.get("modify_ipmoe_mar_check"));
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_2", "modify ipmoe drug, modified item not found : "+ dict.get("modify_ipmoe_mar_check"));
		}
		System.out.println("test_modify_drug_item - END");
	}
	public void test_remove_drug_item() throws Exception {
		System.out.println("test_remove_drug_item - START");
		int number_of_drugs = Integer.parseInt(dict.get("new_ipmoe_drugs_num"));
		int start_num = 1;
		if(test_drug_num > 0){
			start_num = test_drug_num;
			number_of_drugs = test_drug_num;
		}
		//remove drugs
		for(int i=start_num;i<=number_of_drugs;i++){
			String suffix = "_d" + i;
			ip.removeDrug(dict.get("remove_ipmoe_drug_check" + suffix), dict.get("remove_drug_check" + suffix));
		}
		//check existing drugs is null
		String xp = "//table[contains(@id,'gridview-') and contains(@id,'-table')]//tr";
		List<WebElement> li = shared_functions.checkAndGetElementsWhenVisible(By.xpath(xp)); 
		shared_functions.do_screen_capture_with_filename(driver, "T01S12_3");
		if(li==null){
			shared_functions.reporter_ReportEvent("micPass", "QAG_checkpoint_3", "remove ipmoe item - " + number_of_drugs + " drug items removed");
			steps_passed = steps_passed + 1;
		}else{
			shared_functions.reporter_ReportEvent("micFail", "QAG_checkpoint_3", "remove ipmoe item - no items removed");
		}			
		System.out.println("test_remove_drug_item - END");
	}
	
	class PAGE_PatientDetailPage_IPMOEPrescription{
		WebDriver driver = null;
		public PAGE_PatientDetailPage_IPMOEPrescription(WebDriver driver) {
			PageFactory.initElements(driver, this);
			this.driver = driver;
		}
		public void switchToIframe(){
			driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("427Panel");
		}
		public void waitToLoad() throws InterruptedException{
			System.out.println("waitToLoad...");
			String xp = "//*[contains(text(),'Now loading, please wait')]";
			WebDriverWait w = new WebDriverWait(driver, 60); 
			w.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xp)));			
		}
/*		public void waitToLoad_TBD() throws InterruptedException{
			int counter = 0;
			while(isExistNowLoadingPopUp()){
				System.out.println("Now loading...");
				shared_functions.sleepForAWhile(1000);
				counter++;
				if(counter == 60){
					System.out.println("Load over 60s, exit");
					shared_functions.exit_test_iteration();
				}
			}
		}
		public Boolean isExistNowLoadingPopUp_TBD() throws InterruptedException{
			Boolean b = false;
			String xp = "//*[contains(text(),'Now loading, please wait')]";
			List<WebElement> li = shared_functions.getElementsWhenVisible(By.xpath(xp));
			if(li!=null){
				try{
					WebElement e = li.get(0);
					if(e.isDisplayed()){b = true;}else{b =  false;}
				}catch(StaleElementReferenceException e){
					System.out.println("isExistNowLoadingPopUp - StaleElementReferenceException");
					b=false;
					e.printStackTrace();
				}
			}else{
				b =  false;
			}
			System.out.println("isExistNowLoadingPopUp:"+b);
			return b;
		}*/
		public Boolean isExistIpPrescribePage(){
			driver.switchTo().defaultContent();
			String xp = "//iframe[@name='427Panel']";
			List<WebElement> li = shared_functions.checkAndGetElementsWhenVisible(By.xpath(xp));
			return li!=null;
		}
		public Boolean isExistNowLoading(){
			switchToIframe();
			String xp = "//b[contains(text(),'Now loading, please wait')]";
			List<WebElement> li = shared_functions.checkAndGetElementsWhenVisible(By.xpath(xp));
			return li!=null;
		}
		public Boolean isExistFailToRetrievePMSInfo(){
			switchToIframe();
			String xp = "//textarea[contains(text(),'Fail to retrieve PMS information')]";
			List<WebElement> li = shared_functions.checkAndGetElementsWhenVisible(By.xpath(xp));
			return li!=null;
		}
		public Boolean isExistPharmacyInfoOfSpecialty(){
			switchToIframe();
			String xp = "//textarea[contains(text(),'Pharmacy information of specialty')]";
			List<WebElement> li = shared_functions.checkAndGetElementsWhenVisible(By.xpath(xp));
			return li!=null;
		}
		public Boolean isExistFailToRetrieveCMSInfo(){
			switchToIframe();
			String xp = "//textarea[contains(text(),'Fail to retrieve CMS information')]";
			List<WebElement> li = shared_functions.checkAndGetElementsWhenVisible(By.xpath(xp));
			return li!=null;
		}
		public Boolean isExistPreviousRx(){
			switchToIframe();
			String xp = "//a[contains(text(),'Previous Rx')]";
			List<WebElement> li = shared_functions.checkAndGetElementsWhenVisible(By.xpath(xp));
			return li!=null;
		}
		public void inputDrugName(String new_ipmoe_keyword) throws InterruptedException{
			ip.switchToIframe();
			String xp = "//input[contains(@name,'textfield-') and contains(@name,'-inputEl')]";
			List<WebElement> li = shared_functions.getElementsWhenVisible(By.xpath(xp));
			if(li!=null){
				WebElement e = li.get(0);
				e.click();
				shared_functions.clearAndSend(e, new_ipmoe_keyword);
			}
			waitToLoad();
		}
		public void clickAddToMarBtn() throws InterruptedException{
			int idx = 0;
			String xp = "//span[contains(text(),'Add to MAR')]";
			List<WebElement> li = shared_functions.getElementsWhenPresent(By.xpath(xp));
			if(li!=null){
				li.get(idx).click();
				waitToLoad();
			}			
		}
		public void clickAddToMarBtnAfterEdit() throws InterruptedException{
			int idx = 1;
			String xp = "//span[contains(text(),'Add to MAR')]";
			List<WebElement> li = shared_functions.getElementsWhenVisible(By.xpath(xp));
			if(li!=null){
				li.get(idx).click();
				waitToLoad();
			}			
		}
		public void clickEditBtn() throws InterruptedException{
			WebElement e =ip.getWebElementByTagNText("span","Edit");
			e.click();
			waitToLoad();
		}
		public WebElement getWebElementByTagNText(String tag, String str){
			String xp = "//"+tag+"[contains(text(),'"+str+"')]";
			List<WebElement> li = shared_functions.getElementsWhenVisible(By.xpath(xp));
			//System.out.println("selectByString["+tag+","+str+"] size:"+li.size());
			if(li!=null){
				return li.get(0);
			}else{
				System.out.println("getWebElementByTagNText["+tag+","+str+"] is null:"+li.size());
				return null;
			}
		}
		public void selectByTagNText(String tag, String str){
			WebElement e =ip.getWebElementByTagNText(tag,str);
			if(e!=null){
				e.click();
			}else{
				System.out.println("selectByTagNText["+tag+","+str+"] is null");
			}
		}
		public void inputEditDrugFields(List<WebElement> li, String dosage, String frequency, String prn, String route_site, String start_date, String start_time, String end_date, String end_time, String duration, String dose){
			System.out.println("inputEditDrugFields - dosage:"+dosage+", frequency:"+frequency+", prn:"+prn+", route_site:"+route_site+", start_date:"+start_date+", start_time:"+start_time+", end_date:"+end_date+", end_time:"+end_time+", duration:"+duration+", dose:"+dose);
			if(li!=null){
				//Dosage
				if(dosage!=null){
					shared_functions.clearAndSend(li.get(0),dosage);	
				}
				//Frequency
				if(frequency!=null){
					shared_functions.clearAndSend(li.get(1),frequency);	
				}
				//PRN
				if(prn!=null){
					li.get(3).click();	
				}
				//Route/Site
				if(route_site!=null){
					shared_functions.clearAndSend(li.get(4),route_site);
				}
				//Start Date
				if(start_date!=null){
					shared_functions.clearAndSend(li.get(5),start_date);
				}
				//Start Time
				if(start_time!=null){
					shared_functions.clearAndSend(li.get(6),start_time);
				}else{
					shared_functions.clearAndSend(li.get(6),one_hour_laterhhmm);
				}
				if( !frequency.equals("ONCE") ){
					//End Date
					if(end_date!=null){
						shared_functions.clearAndSend(li.get(7),end_date);
					}
					//End Time
					if(end_time!=null){
						shared_functions.clearAndSend(li.get(8),end_time);
					}								
				}
				//Duration/Dose
				if(duration!=null){
					shared_functions.clearAndSend(li.get(9),duration);
				}
				//Duration/Dose Unit
				if(dose!=null){
					li.get(10).click();
					ip.selectByTagNText("li", dose);	
				}
			}	
		}
		public void inputModifyDrugFields(List<WebElement> li, String dosage, String frequency, String prn, String route_site, String start_date, String start_time, String end_date, String end_time, String duration, String dose) throws InterruptedException{
			System.out.println("inputModifyDrugFields - dosage:"+dosage+", frequency:"+frequency+", prn:"+prn+", route_site:"+route_site+", start_date:"+start_date+", start_time:"+start_time+", end_date:"+end_date+", end_time:"+end_time+", duration:"+duration+", dose:"+dose);
			if(li!=null){
				//Dosage
				if(dosage!=null){
					shared_functions.clearAndSend(li.get(0),dosage);	
				}
				//Frequency
				if(frequency!=null){
					shared_functions.clearAndSend(li.get(1),frequency);
					shared_functions.sleepForAWhile(1000);
				}
				//PRN
				if(prn!=null){
					if(prn.equals("N")){
						li.get(3).click();
					}
				}
				//Route/Site
				if(route_site!=null){
					shared_functions.clearAndSend(li.get(4),route_site);
				}
				//Start Date
				if(start_date!=null){
					shared_functions.clearAndSend(li.get(5),start_date);
				}
				//Start Time
				if(start_time!=null){
					shared_functions.clearAndSend(li.get(6),start_time);
				}
				if( !frequency.equals("ONCE") ){
					//End Date
					if(end_date!=null){
						shared_functions.clearAndSend(li.get(7),end_date);
					}
					//End Time
					if(end_time!=null){
						shared_functions.clearAndSend(li.get(8),end_time);
					}								
				}
				//Duration/Dose
				if(duration!=null){
					shared_functions.clearAndSend(li.get(9),duration);
				}
				//Duration/Dose Unit
				if(dose!=null){
					li.get(10).click();
					ip.selectByTagNText("li", dose);	
				}
			}	
		}
		public void selectDeleteFromList() throws InterruptedException{
			System.out.println("selectDeleteFromList");
			String xp = "//span[contains(text(),'Delete') and contains(@class,'x-menu-item-text x-menu-item-indent')]";
			List<WebElement> li = shared_functions.getElementsWhenVisible(By.xpath(xp));
			if(li!=null){
				li.get(0).click();
				waitToLoad();
			}
		}
		public void cannotDeleteWaitForPharmacyProcess(){
			System.out.println("cannotDeleteWaitForPharmacyProcess");
			String xp3 = "//span[contains(text(),'K')]/u[contains(text(),'O')]";
			List<WebElement> li3 = shared_functions.getElementsWhenVisible(By.xpath(xp3));
			if(li3!=null){
				li3.get(0).click();
			}			
		}
		public void confirmDelete(String strDrugToRemove) throws InterruptedException{
			System.out.println("confirmDelete:"+strDrugToRemove);
			strDrugToRemove = strDrugToRemove.replace("\\", "");
			String xp = "//textarea[contains(text(),'"+strDrugToRemove+"')]";
			List<WebElement> li = shared_functions.getElementsWhenVisible(By.xpath(xp));
			if(li!=null){
				clickDeleteBtn();
			}			
		}
		public void clickDeleteBtn() throws InterruptedException{
			String xp = "//span[contains(text(),'Delete') and contains(@class,'x-btn-inner x-btn-inner-center')]";
			List<WebElement> li = shared_functions.getElementsWhenVisible(By.xpath(xp));
			if(li!=null){
				li.get(0).click();
				waitToLoad();
			}			
		}
		public WebElement select_today_drug_in_mar(String mar_description){
			System.out.println("select_today_drug_in_mar["+mar_description+"] START");
			ip.switchToIframe();
			WebElement retval = null;
			try{
				String xp = "//table[contains(@id,'gridview-') and contains(@id,'-table')]";
				List<WebElement> li = shared_functions.getElementsWhenPresent(By.xpath(xp));
				//System.out.println("select_today_drug_in_mar, li size:"+li.size());
				int idxTable = 1;
				WebElement all_drugs_table = li.get(idxTable);
				//int sizeOfTr = all_drugs_table.findElements(By.tagName("tr")).size();
				int sizeOfTr = shared_functions.getElementsInsideParentWebElementWhenVisible(all_drugs_table, By.tagName("tr")).size();
				//System.out.println("select_today_drug_in_mar, sizeOfTr:"+sizeOfTr);
				for(int d=0; d<sizeOfTr; d++){
					String strDateInCMS = shared_functions.getElementFromTable(all_drugs_table,d,1).getText();
					String strDrugInCMS = shared_functions.getElementFromTable(all_drugs_table,d,2).getText().replace("\n", "");
					Boolean b1 = (strDateInCMS.indexOf(today_ddMMM)!=-1);
					Boolean b2 = (strDrugInCMS.indexOf(mar_description)!=-1);
					//System.out.println("select_today_drug_in_mar today_ddMMM:"+today_ddMMM);
					//System.out.println("select_today_drug_in_mar strDateInCMS:"+strDateInCMS);
					//System.out.println("select_today_drug_in_mar b1:"+b1);
					//System.out.println("select_today_drug_in_mar strDrugInCMS:"+strDrugInCMS);
					//System.out.println("select_today_drug_in_mar mar_description:"+mar_description);
					//System.out.println("select_today_drug_in_mar b2:"+b2);
					if(b1&&b2){
						//retval = all_drugs_table.findElements(By.tagName("tr")).get(d).findElements(By.tagName("td")).get(2);
						List<WebElement> li1 = shared_functions.getElementsInsideParentWebElementWhenVisible(all_drugs_table, By.tagName("tr"));
						//System.out.println("select_today_drug_in_mar, li1 size:"+li1.size());
						List<WebElement> li2 = shared_functions.getElementsInsideParentWebElementWhenVisible(li1.get(d), By.tagName("td"));
						//System.out.println("select_today_drug_in_mar, li2 size:"+li2.size());
						retval = li2.get(2);
						System.out.println("select_today_drug_in_mar retval:"+retval);
						retval.click();
					}
				}
			}catch(StaleElementReferenceException ex){ 
				System.out.println("select_today_drug_in_mar, StaleElementReferenceException");
				return select_today_drug_in_mar(mar_description);
			}
			shared_functions.reporter_ReportEvent("micDone", "select_today_drug_in_mar", "selected " + mar_description);
			System.out.println("select_today_drug_in_mar["+mar_description+"] END");
			return retval;
		}
		public String get_new_drug_panel_description(){
			System.out.println("get_new_drug_panel_description");
			ip.switchToIframe();
			String retval = "NOT FOUND";
			String xp = "//div[contains(@class,'x-panel-body x-panel-body-noheader')]";
			List<WebElement> li = shared_functions.checkAndGetElementsWhenPresent(By.xpath(xp));
			int size = 0;
			if(li!=null){size=li.size();}
			System.out.println("get_new_drug_panel_description,size:"+size);
			for(int i=0;i<size;i++){
				WebElement the_panel = li.get(i);
				System.out.println("width["+i+"]"+Integer.parseInt(the_panel.getAttribute("width")));
				if( Integer.parseInt(the_panel.getAttribute("width")) >950){
					retval = the_panel.getText();
					System.out.println("retval:"+retval);
				}
			}
			return retval;
		}
		public void removeDrug(String remove_ipmoe_drug_check, String remove_drug_check) throws InterruptedException{
			System.out.println("removeDrug,remove_ipmoe_drug_check["+remove_ipmoe_drug_check+"]");
			System.out.println("removeDrug,remove_drug_check["+remove_drug_check+"]");
			try{
				WebElement drug_to_remove = ip.select_today_drug_in_mar(remove_ipmoe_drug_check);
				if(drug_to_remove!=null){
					shared_functions.right_click(drug_to_remove, 0, 10);
					selectDeleteFromList();		
					String str = "Are you sure you want to delete " + remove_drug_check + " from patient";
					confirmDelete(str);							
				}else{
					System.out.println("cannot remove drug:"+remove_ipmoe_drug_check);
				}
			}catch(StaleElementReferenceException e){ //prevent StaleElementReferenceException
				System.out.println("removeDrug, StaleElementReferenceException:"+remove_ipmoe_drug_check);
				removeDrug(remove_ipmoe_drug_check, remove_drug_check);
			}catch(TimeoutException e){ //cannot  delete due to pharmacy process
				System.out.println("removeDrug, TimeoutException");
				cannotDeleteWaitForPharmacyProcess();
				removeDrug(remove_ipmoe_drug_check, remove_drug_check);
			}
		}
		/*
		public Boolean checkIfBeingProcessedByPharmacy() {
			Boolean bProcessing = true;
			String xp2 = "//textarea[contains(text(),'The order is being processed by pharmacy. It cannot be deleted.')]";
			List<WebElement> li2 = null;
			try{
				li2 = shared_functions.checkAndGetElementsWhenVisible(By.xpath(xp2));
			}catch(StaleElementReferenceException ex){
				ex.printStackTrace();
				return checkIfBeingProcessedByPharmacy();
			}
			if(li2!=null){ bProcessing = true; }else{ bProcessing = false; }
			return bProcessing;
		}
		*/
	}
}
