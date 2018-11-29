package suite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PAGE_CMSMainPage {
	WebDriver driver = null;
	List<WebElement> li_top_menu = null;
	public PAGE_CMSMainPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}
	public void getTopMenu() {
		driver.switchTo().defaultContent();
		//li_top_menu = driver.findElements(By.cssSelector("#cmsMenuBar>table>tbody>tr>td")); //NLT
		li_top_menu = driver.findElements(By.cssSelector("#cmsMenuBar a.x-btn"));
	}
	public void fnNextPatient() throws InterruptedException{
		String str = "Next Patient";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eFile = li_top_menu.get(0);
		eFile.click();
		driver.findElement(By.linkText(str)).click();
		shared_functions.sleepForAWhile(3000);
		//if( shared_functions.isAlertPresent(driver) ) {shared_functions.handleAlert(driver, false);}
	}
	public void fnClose() throws InterruptedException {
		String str = "Close";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement e = li_top_menu.get(0);
		e.click();
		driver.findElement(By.linkText(str)).click();
		if( shared_functions.isAlertPresent(driver) ) {
			shared_functions.handleAlert(driver, false);
		}
	}
	public void fnLogoff(){
		String str = "Logoff";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eFile = li_top_menu.get(0);
		eFile.click();
		driver.findElement(By.linkText(str)).click();
		driver.quit();	   
	}
	public void fnPMI() {
		String str = "Patient Master Index (PMI)";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eEnquiry = li_top_menu.get(3);
		eEnquiry.click();
		driver.findElement(By.linkText(str)).click();
	}
	public void fnDischargeSummary() {
		String str = "Discharge Summary";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eClinical = li_top_menu.get(1);
		eClinical.click();		
		driver.findElement(By.linkText(str)).click();
	}
	public void fnIxRequest() {
		String str = "Ix Request";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eInvestigation = li_top_menu.get(2);
		eInvestigation.click();		
		driver.findElement(By.linkText(str)).click();
	}
	public void fnBedAssignment() {
		String str = "Bed Assignment";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		driver.findElement(By.linkText(str)).click();
	}
	public void fnTransfer() {
		String str = "Transfer";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		driver.findElement(By.linkText(str)).click();
	}
	public void fnSwapBed() {
		String str = "Swap Bed";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		driver.findElement(By.linkText(str)).click();
	}
	public void fnDischarge() {
		String str = "Discharge";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		driver.findElement(By.linkText(str)).click();
	}
	public void fnModificationOfTransfer() {
		String str = "Modification of Transfer";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();
		driver.findElement(By.linkText(str)).click();
	}
	public void fnModificationOfDischarge() {
		String str = "Modification of Discharge";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		driver.findElement(By.linkText(str)).click();
	}
	public void fnTrialDischarge() {
		String str = "Trial Discharge";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		driver.findElement(By.linkText(str)).click();
	}
	public void fnReturnFromTrialDischarge() {
		String str = "Return from Trial Discharge";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		driver.findElement(By.linkText(str)).click();
	}
	public void fnCancelPreviousTransaction() {
		String str = "Cancellation of Previous Transaction";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();
		driver.findElement(By.linkText(str)).click();
	}
	public void fnLetterDocument() {
		String str = "Letter/Document";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDocPrint = li_top_menu.get(7);
		eDocPrint.click();
		driver.findElement(By.linkText(str)).click();
	}
	public void fnMoe() {
		String str = "Discharge Prescription/OP Prescription";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement e = li_top_menu.get(1);
		e.click();
		driver.findElement(By.linkText(str)).click();
	}
	public void fnIpPrescribing() {
		String str = "IP Prescribing";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement e = li_top_menu.get(1);
		e.click();
		driver.findElement(By.linkText(str)).click();
	}
	public void fnDrugAdminByPatient() {
		String str = "Drug Admin by Patient";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement e = li_top_menu.get(1);
		e.click();
		driver.findElement(By.linkText(str)).click();
	}
	public WebElement getRefreshBtn() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("cmsPSPWinPanelCmp");
		String str = "#pspRefreshBtn-frame1Table";
		Boolean isExist = driver.findElements(By.cssSelector(str)).size()>0;
		if(isExist) {
			WebElement e = driver.findElement(By.cssSelector(str));
			return driver.findElement(By.cssSelector(str));
		}
		return null;
	}
	public WebElement getPSPTable(int idxTable) {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("cmsPSPWinPanelCmp");
		List<WebElement> liTable = driver.findElements(By.cssSelector("table.x-grid-table"));
		WebElement eTable = liTable.get(idxTable);
		return eTable;
	}
	public List<WebElement> getPSPRowsAtTable(int idxTable) {//0 left 1 right
		WebElement eTable = this.getPSPTable(idxTable);
		WebElement eTbody = eTable.findElements(By.cssSelector("tbody")).get(0);
		List<WebElement> liPatientRows = eTbody.findElements(By.cssSelector("tr"));
		return liPatientRows;
	}
	public int getPSPRowNum() {
		return this.getPSPRowsAtTable(0).size();
	}
	public List<WebElement> getPSPLeftColumnElement(int idxPatient) {
		WebElement ePatientRow = this.getPSPRowsAtTable(0).get(idxPatient);
		if(!ePatientRow.isDisplayed()) {
			ePatientRow.click();
		}
		List<WebElement>  liColumns = ePatientRow.findElements(By.cssSelector("td div font"));
		//for(WebElement col: liColumns) {System.out.println( col.getText() );}
		return liColumns;
	}
	public List<WebElement> getPSPRightColumnElement(int idxPatient) {
		WebElement ePatientRow = this.getPSPRowsAtTable(1).get(idxPatient);
		List<WebElement>  liColumns = ePatientRow.findElements(By.cssSelector("td div"));
		//for(WebElement col: liColumns) {System.out.println( col.getText() );}
		return liColumns;
	}
	public List<WebElement> getPSPValue(int idxPatient){
		List<WebElement> liAllColVal = new ArrayList();
		liAllColVal.addAll(this.getPSPLeftColumnElement(idxPatient));
		liAllColVal.addAll(this.getPSPRightColumnElement(idxPatient));
		return liAllColVal;
	}
	public int getPatientIdxByCaseNum(String caseNum) {
		int idxOfCol = 1; //CaseNum
		int idxOfPatientRow = -1;
		List<WebElement> liPatientRows = this.getPSPRowsAtTable(1);
		for(int i=0; i<liPatientRows.size(); i++) {
			WebElement ePatient = liPatientRows.get(i);
			List<WebElement>  liColumns = ePatient.findElements(By.cssSelector("td div"));
			String episodeNum = liColumns.get(idxOfCol).getText();
			if(episodeNum.equals(caseNum)) { idxOfPatientRow=i; }
		}
		return idxOfPatientRow;
	}
	public String getBedNumByCaseNum(String caseNum) {
		int idxOfCol = 0; //BedNum
		int patientRowId = this.getPatientIdxByCaseNum(caseNum);
		List<WebElement> liLeftPatientData = this.getPSPLeftColumnElement(patientRowId);
		String strBedNum = liLeftPatientData.get(idxOfCol).getText();
		return strBedNum;
	}
	public String getCategoryByCaseNum(String caseNum) { 
		int idxOfCol = 3; //CI
		int patientRowId = this.getPatientIdxByCaseNum(caseNum);
		List<WebElement> liPatientRightValue = this.getPSPRightColumnElement(patientRowId);
		String strCI = liPatientRightValue.get(idxOfCol).getText(); 
		return strCI;
	}
	public String getSpecialtyByCaseNum(String caseNum) { 
		int idxOfCol = 2; //Specialy
		int patientRowId = this.getPatientIdxByCaseNum(caseNum);
		List<WebElement> liPatientRightValue = this.getPSPRightColumnElement(patientRowId);
		String strCI = liPatientRightValue.get(idxOfCol).getText(); 
		return strCI;
	}
	public WebElement getOKBtnInPopUpWindow() {
		String xp = "//span[contains(text(),'K')]//u[contains(text(),'O')]";
		WebElement e = driver.findElement(By.xpath(xp));
		return e;
	}
	public void selectPatientList(int idx) {
		System.out.println("PAGE - CMS - Select Patient List");
		   	driver.switchTo().defaultContent();
			driver.switchTo().frame("cmsPSPWinPanelCmp");
			
			String cssPSP = "#pspMainBtnPanel";
			String cssDropDownBtn = "#pspMainBtnPanel table.x-field td.x-form-item-body td.x-trigger-cell.x-unselectable";
			driver.findElement(By.cssSelector(cssPSP+cssDropDownBtn)).click();
			
			String cssListItems = "div.x-boundlist li.x-boundlist-item";
			List<WebElement> liItems = driver.findElements(By.cssSelector( cssListItems ));
			/*DEBUG
			System.out.println("liItems:"+liItems.size());
			for(int i =0; i<liItems.size(); i++ ) {
				String str = liItems.get(i).getText();
				System.out.println("List("+i+"):"+str);
			}*/
			
			liItems.get(idx).click(); //SELECT PATIENT LIST 'Normal Patient List'	
	}
	
	/*public void selectWard1(int idxOfPatientList, String strWard) {
			System.out.println("PAGE - CMS - Select Ward");
		    driver.switchTo().defaultContent();
			driver.switchTo().frame("cmsPSPWinPanelCmp");

			String cssPSP = "#pspSelectionPanel"+idxOfPatientList+" ";
			String cssDropDownBtn = "table.x-field td.x-form-item-body td.x-trigger-cell.x-unselectable";
			driver.findElement(By.cssSelector(cssPSP+cssDropDownBtn)).click();
			
			String cssListItems = "div.x-boundlist li.x-boundlist-item";
			List<WebElement> liItems = driver.findElements(By.cssSelector( cssListItems )); 

			//DEBUG System.out.println("liItems:"+liItems.size());
			//DEBUG for(int i =0; i<liItems.size(); i++ ) {
				//DEBUG String str = liItems.get(i).getText();
				//DEBUG System.out.println("List("+i+"):"+str);
			//DEBUG }
			
			int idx = -1;
			for(int i =0; i<liItems.size(); i++ ) {
				if( liItems.get(i).getText().equals(strWard)) {
					idx = i;
					break;
				}
			}
			
			if(idx == -1) {
				shared_functions.reporter_ReportEvent("micWarning", "change_ward - not exists", "ward does not exist");
			}else {
				liItems.get(idx).click(); //SELECT WARD LIST '8A'
				shared_functions.reporter_ReportEvent("micDone", "change_ward - yes", "changed to ward: "+strWard);
				System.out.println("changed to ward: "+strWard);
			}

	}
	public void changeWard(int idxOfPatientList,String strWard) {
		System.out.println("PAGE - CMS - Change Ward");
	    driver.switchTo().defaultContent();
		driver.switchTo().frame("cmsPSPWinPanelCmp");
		String cssPSP = "#pspSelectionPanel"+idxOfPatientList+" "; //"#cuicombobox-1026-inputEl";
		String cssWardField = "table.x-field td.x-form-item-body td.x-form-trigger-input-cell input";
		List<WebElement> liWardField = driver.findElements(By.cssSelector(cssPSP+cssWardField));

		for(int i =0; i<liWardField.size(); i++ ) {
			WebElement eWardField = liWardField.get(i);
			if( eWardField.isDisplayed() ) {
				shared_functions.clearAndSend(eWardField, strWard);
			}
		}
	}*/
	public void changeWardForNormalPatientList(String strWard) {
		System.out.println("PAGE - CMS - changeWardForNormalPatientList "+strWard);
	    driver.switchTo().defaultContent();
		driver.switchTo().frame("cmsPSPWinPanelCmp");
		String cssWard = "#cuicombobox-1026-inputEl";
		WebElement e = driver.findElement(By.cssSelector(cssWard));
		e.click();
		e.clear();
		e.click();
		e.sendKeys(Keys.chord(Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE));
		e.sendKeys(strWard);
		e.sendKeys(Keys.ENTER);
	}
	public void selectPatient(int idxOfPatient) {
	    driver.switchTo().defaultContent();
		driver.switchTo().frame("cmsPSPWinPanelCmp");	   
		System.out.println("PAGE - CMS - Select Patient");
		int i1 = driver.findElements(By.cssSelector("#pspPatientListPanel0 div.x-box-target>div.x-panel")).get(0).findElements(By.cssSelector("div.x-panel-body tr.x-grid-row")).size();
		List<WebElement> li_patients = driver.findElements(By.cssSelector("#pspPatientListPanel0 div.x-box-target>div.x-panel")).get(0).findElements(By.cssSelector("div.x-panel-body tr.x-grid-row"));
		/*DEBUG
		for(int i =0; i<li_patients.size(); i++ ) {
			System.out.println( i+":"+li_patients.get(i).findElements(By.cssSelector("td")).get(1).findElement(By.tagName("font")).getText() );
		} */
		WebElement ePatient = li_patients.get(idxOfPatient); //Select Patients
		String patientName = ePatient.findElements(By.cssSelector("td")).get(1).findElement(By.tagName("font")).getText();
		System.out.println("Patient Name: "+patientName);
		ePatient.click();
		//WebElement eSelectPatient = driver.findElement(By.cssSelector("#topPanel #topUpPanel #pspSelectBtn")); //NLT
		WebElement eSelectPatient = driver.findElement(By.cssSelector("#pspSelectBtn"));
		JavascriptExecutor js = (JavascriptExecutor) driver; 
		js.executeScript("arguments[0].scrollIntoView();", eSelectPatient); 
		eSelectPatient.click(); //Click 'SELECT'
	}
	public void selectPatientByCaseNum(String patientCaseNum) {
	    driver.switchTo().defaultContent();
		driver.switchTo().frame("cmsPSPWinPanelCmp");	   
		System.out.println("PAGE - CMS - Select Patient "+patientCaseNum);
		WebElement ePatientCaseNumInput = driver.findElement(By.cssSelector("#pspSearchTextField td.x-form-item-body input"));
		ePatientCaseNumInput.sendKeys( patientCaseNum );
		ePatientCaseNumInput.sendKeys( Keys.ENTER );
	}
	public void handleSelectPatientSpecialtyPopUpWindow() {
		driver.switchTo().defaultContent();
		if( driver.findElements(By.cssSelector("#CMSIpPatSpecWin")).size()>0 ) {
			System.out.println("There is Select Patient Specialty popup window, idx: Nil");
			driver.findElement(By.cssSelector("#CMSIpPatSpecWin #CMSIpPatSpecCurrBtn button")).click();
		}else {
			System.out.println("There is no Select Patient Specialty popup window");
		}
	}
	public void handleSelectPatientSpecialtyPopUpWindow(int idx) {
		driver.switchTo().defaultContent();
		if( driver.findElements(By.cssSelector("#CMSIpPatSpecWin")).size()>0 ) {
			System.out.println("There is Select Patient Specialty popup window, idx: "+idx);
				driver.findElements(By.cssSelector("#CMSIpPatSpecWin #ipSpecGrid .x-grid3-scroller .x-grid3-row")).get(idx).findElement(By.cssSelector(".x-grid3-cell-inner.x-grid3-col-specName")).click();
				driver.findElement(By.cssSelector("#CMSIpPatSpecWin #CMSIpPatSpecBtn button")).click();
		}else {
			System.out.println("There is no Select Patient Specialty popup window");
		}
	}
	public void selectSpecialty(String spec, String clinic) {
		driver.switchTo().defaultContent();
		String xp = "//div[contains(text(),'"+spec+"')]";
		List<WebElement> li = driver.findElements(By.xpath(xp));
		if(li.size()>0){
			li.get(0).click();
		}		
		String xp2 = "//div[contains(text(),'"+clinic+"')]";
		List<WebElement> li2 = driver.findElements(By.xpath(xp2));
		if(li2.size()>0){
			li2.get(0).click();
		}		
		String xp3 = "//span[@id='CMSOpPatSpecSelectBtn-btnInnerEl']";
		List<WebElement> li3 = driver.findElements(By.xpath(xp3));
		if(li3.size()>0){
			li3.get(0).click();
		}				
	}	
	
	/*
	 	public void closeAlertPanel_toBeDelete() {
		driver.switchTo().defaultContent();
		Boolean openByDetailBtnOfExistingAlertReminderWindow = (driver.findElements(By.cssSelector("iframe[name=alertWinPanelDetailModalIFrame]")).size()>0);
		Boolean openByAlertBtn = (driver.findElements(By.cssSelector("iframe[name=alertWinPanelModalIFrame]")).size()>0);
		if( openByDetailBtnOfExistingAlertReminderWindow ) {
			driver.switchTo().frame("alertWinPanelDetailModalIFrame");				
		}else if( openByAlertBtn ){
			driver.switchTo().frame("alertWinPanelModalIFrame");				
		}
		WebElement eAlertPanelCloseBtn = driver.findElement(By.cssSelector("#btnCorpAllergyClose"));
		eAlertPanelCloseBtn.click();
	}

	 */
}
