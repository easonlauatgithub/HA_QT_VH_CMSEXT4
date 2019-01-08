package suite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
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
		li_top_menu = shared_functions.getElementsWhenVisible(By.cssSelector("#cmsMenuBar a.x-btn"));
	}
	public void fnNextPatient() throws InterruptedException{
		String str = "Next Patient";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eFile = li_top_menu.get(0);
		eFile.click();
		shared_functions.clickElementWhenClickable(By.linkText(str));
		cancelListOfPrescriptionsModifiedByPharmacy();
		shared_functions.sleepForAWhile(3000);
	}
	public void cancelListOfPrescriptionsModifiedByPharmacy(){
		driver.switchTo().defaultContent();
		List<WebElement> liFrame = null;
		try{
			WebDriverWait w = new WebDriverWait(driver, 5);
			liFrame = w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("iframe[name=alertWinPanelModalIFrame]")));
		}catch(TimeoutException e){
			System.out.println("cancelListOfPrescriptionsModifiedByPharmacy, TimeoutException");
		}
		if(liFrame!=null){
			System.out.println("cancelListOfPrescriptionsModifiedByPharmacy - switch to iframe 230ModalIFrame");
			shared_functions.switchToFrameByString("230ModalIFrame");
			shared_functions.clickElementWhenClickable(By.cssSelector("#pRmkUserCancelBtn"));	
		}
	}
	public void fnClose() throws InterruptedException {
		String str = "Close";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement e = li_top_menu.get(0);
		e.click();
		shared_functions.clickElementWhenClickable(By.linkText(str));
		shared_functions.checkExistAndHandleAlert(driver, false);
	}
	public void fnLogoff(){
		String str = "Logoff";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eFile = li_top_menu.get(0);
		eFile.click();
		shared_functions.clickElementWhenClickable(By.linkText(str));
		driver.quit();
	}
	public void fnPMI() {
		String str = "Patient Master Index (PMI)";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eEnquiry = li_top_menu.get(3);
		eEnquiry.click();
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnDischargeSummary() {
		String str = "Discharge Summary";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eClinical = li_top_menu.get(1);
		eClinical.click();		
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnIxRequest() {
		String str = "Ix Request";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eInvestigation = li_top_menu.get(2);
		eInvestigation.click();		
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnBedAssignment() {
		String str = "Bed Assignment";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnTransfer() {
		String str = "Transfer";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnSwapBed() {
		String str = "Swap Bed";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnDischarge() {
		String str = "Discharge";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnModificationOfTransfer() {
		String str = "Modification of Transfer";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnModificationOfDischarge() {
		String str = "Modification of Discharge";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnTrialDischarge() {
		String str = "Trial Discharge";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnReturnFromTrialDischarge() {
		String str = "Return from Trial Discharge";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();		
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnCancelPreviousTransaction() {
		String str = "Cancellation of Previous Transaction";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDT = li_top_menu.get(5);
		eDT.click();
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnLetterDocument() {
		String str = "Letter/Document";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement eDocPrint = li_top_menu.get(7);
		eDocPrint.click();
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnMoe() {
		String str = "Discharge Prescription/OP Prescription";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement e = li_top_menu.get(1);
		e.click();
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnIpPrescribing() {
		String str = "IP Prescribing";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement e = li_top_menu.get(1);
		e.click();
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public void fnDrugAdminByPatient() {
		String str = "Drug Admin by Patient";
		System.out.println("PAGE - CMS - "+str);
		getTopMenu();
		WebElement e = li_top_menu.get(1);
		e.click();
		shared_functions.clickElementWhenClickable(By.linkText(str));
	}
	public WebElement getRefreshBtn() {
		driver.switchTo().defaultContent();
		shared_functions.switchToFrameByString("cmsPSPWinPanelCmp");
		String str = "#pspRefreshBtn-frame1Table";
		try{
			WebElement e = shared_functions.getElementWhenClickable(By.cssSelector(str));
			return e;
		}catch(TimeoutException ex){
			ex.printStackTrace();
		}
		return null;
	}
	public WebElement getPSPTable(int idxTable) {
		driver.switchTo().defaultContent();
		shared_functions.switchToFrameByString("cmsPSPWinPanelCmp");
		List<WebElement> liTable = shared_functions.getElementsWhenPresent(By.cssSelector("table.x-grid-table"));
		WebElement eTable = liTable.get(idxTable);
		return eTable;
	}
	public List<WebElement> getPSPRowsFromTable(int idxTable) {//0 left 1 right
		WebElement eTable = this.getPSPTable(idxTable);
		List<WebElement> liTBodies = shared_functions.getElementsInsideParentWebElementWhenVisible(eTable, By.cssSelector("tbody"));
		WebElement eTbody = liTBodies.get(0);
		List<WebElement> liPatientRows = shared_functions.getRowsFromTable(eTbody);
		return liPatientRows;
	}
	public int getNumOfPSPRows() {
		return this.getPSPRowsFromTable(0).size();
	}
	public List<WebElement> getPSPLeftColumnElement(int idxPatient) {
		System.out.println("getPSPLeftColumnElement");
		WebElement eRow = this.getPSPRowsFromTable(0).get(idxPatient);
		if(!eRow.isDisplayed()) {
			eRow.click();
		}
		shared_functions.Hardcode("cannot use getElementsInsideParentWebElementWhenVisible, getElementsInsideParentWebElementWhenPresent"); 
		//List<WebElement> liCols = shared_functions.getElementsInsideParentWebElementWhenVisible(eRow, By.cssSelector("td div font"));
		//List<WebElement> liCols = shared_functions.getElementsInsideParentWebElementWhenPresent(eRow, By.cssSelector("td div font"));
		List<WebElement> liCols = eRow.findElements(By.cssSelector("td div font"));
		//DEBUG for(WebElement col: liCols) {System.out.println( col.getText() );}
		return liCols;
	}
	public List<WebElement> getPSPRightColumnElement(int idxPatient) {
		WebElement eRow = this.getPSPRowsFromTable(1).get(idxPatient);
		List<WebElement> liCols = shared_functions.getElementsInsideParentWebElementWhenVisible(eRow, By.cssSelector("td div"));
		//List<WebElement> liCols = eRow.findElements(By.cssSelector("td div"));
		//DEBUG for(WebElement col: liCols) {System.out.println( col.getText() );}
		return liCols;
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
		List<WebElement> liPatientRows = this.getPSPRowsFromTable(1);
		for(int i=0; i<liPatientRows.size(); i++) {
			WebElement ePatient = liPatientRows.get(i);
			List<WebElement>  liColumns = shared_functions.getElementsInsideParentWebElementWhenVisible(ePatient, By.cssSelector("td div"));
			String episodeNum = "";
			try{
				episodeNum = liColumns.get(idxOfCol).getText();	
			}catch(StaleElementReferenceException ex){
				System.out.println("getPatientIdxByCaseNum,StaleElementReferenceException");
			}
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
		WebElement e = shared_functions.getElementWhenClickable(By.xpath(xp));
		return e;
	}
	public void selectPatientList(int idx) {
		System.out.println("PAGE - CMS - Select Patient List");
		   	driver.switchTo().defaultContent();
			shared_functions.switchToFrameByString("cmsPSPWinPanelCmp");
			
			String cssPSP = "#pspMainBtnPanel";
			String cssDropDownBtn = "#pspMainBtnPanel table.x-field td.x-form-item-body td.x-trigger-cell.x-unselectable";
			shared_functions.clickElementWhenClickable(By.cssSelector(cssPSP+cssDropDownBtn));
			String cssListItems = "div.x-boundlist li.x-boundlist-item";
			List<WebElement> liItems = shared_functions.getElementsWhenVisible(By.cssSelector( cssListItems ));
			/*DEBUG
			System.out.println("liItems:"+liItems.size());
			for(int i =0; i<liItems.size(); i++ ) {
				String str = liItems.get(i).getText();
				System.out.println("List("+i+"):"+str);
			}*/
			liItems.get(idx).click(); //SELECT PATIENT LIST 'Normal Patient List'	
	}
	public void changeWardForNormalPatientList(String strWard) throws InterruptedException {
		System.out.println("PAGE - CMS - changeWardForNormalPatientList "+strWard);
	    driver.switchTo().defaultContent();
		shared_functions.switchToFrameByString("cmsPSPWinPanelCmp");
		String cssWard = "#cuicombobox-1026-inputEl";
		WebElement e = shared_functions.getElementWhenClickable(By.cssSelector(cssWard)); 
		try{
			e.click();
			e.clear();
			e.click();
			e.sendKeys(Keys.chord(Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE));
			e.sendKeys(strWard);
			e.sendKeys(Keys.ENTER);
		}catch(StaleElementReferenceException ex){
			ex.printStackTrace();
			changeWardForNormalPatientList(strWard);
		}
		shared_functions.sleepForAWhile(1000);
	}
	/*
	public void selectPatien_TBD(int idxOfPatient) {
	    driver.switchTo().defaultContent();
		shared_functions.switchToFrameByString("cmsPSPWinPanelCmp");	   
		System.out.println("PAGE - CMS - Select Patient");
		List<WebElement> li = shared_functions.getElementsWhenVisible(By.cssSelector("#pspPatientListPanel0 div.x-box-target>div.x-panel"));
		List<WebElement> li_patients = shared_functions.getElementsInsideParentWebElementWhenVisible(li.get(0), By.cssSelector("div.x-panel-body tr.x-grid-row"));
		int i1 = li_patients.size();
		//DEBUG
		//for(int i =0; i<li_patients.size(); i++ ) {
		//	List<WebElement> li2 = shared_functions.getElementsInsideParentWebElementWhenVisible(li_patients.get(i),By.cssSelector("td"));
		//	List<WebElement> li3 = shared_functions.getElementsInsideParentWebElementWhenVisible(li2.get(1),By.tagName("font"));
		//	System.out.println( i+":"+li3.get(0).getText() );
		//}
		WebElement ePatient = li_patients.get(idxOfPatient); //Select Patients
		List<WebElement> li4 = shared_functions.getElementsInsideParentWebElementWhenVisible(ePatient,By.cssSelector("td"));
		List<WebElement> li5 = shared_functions.getElementsInsideParentWebElementWhenVisible(li4.get(1),By.tagName("font"));
		String patientName = li5.get(0).getText();
		System.out.println("Patient Name: "+patientName);
		ePatient.click();
		WebElement eSelectPatient = shared_functions.getElementWhenClickable(By.cssSelector("#pspSelectBtn"));
		JavascriptExecutor js = (JavascriptExecutor) driver; 
		js.executeScript("arguments[0].scrollIntoView();", eSelectPatient); 
		eSelectPatient.click(); //Click 'SELECT'
	}
	*/
	public void selectPatientByCaseNum(String patientCaseNum) {
	    driver.switchTo().defaultContent();
		shared_functions.switchToFrameByString("cmsPSPWinPanelCmp");	   
		System.out.println("PAGE - CMS - Select Patient "+patientCaseNum);
		WebElement ePatientCaseNumInput = shared_functions.getElementWhenClickable(By.cssSelector("#pspSearchTextField td.x-form-item-body input"));
		ePatientCaseNumInput.sendKeys( patientCaseNum );
		ePatientCaseNumInput.sendKeys( Keys.ENTER );
	}
	public void handleSelectPatientSpecialtyPopUpWindow() {
		driver.switchTo().defaultContent();
		if( shared_functions.getElementsWhenVisible(By.cssSelector("#CMSIpPatSpecWin")).size()>0 ) {
			System.out.println("There is Select Patient Specialty popup window, idx: Nil");
			shared_functions.getElementWhenClickable(By.cssSelector("#CMSIpPatSpecWin #CMSIpPatSpecCurrBtn button")).click();
		}else {
			System.out.println("There is no Select Patient Specialty popup window");
		}
	}
	public void handleSelectPatientSpecialtyPopUpWindow(int idx) {
		driver.switchTo().defaultContent();
		if( shared_functions.getElementsWhenVisible(By.cssSelector("#CMSIpPatSpecWin")).size()>0 ) {
			System.out.println("There is Select Patient Specialty popup window, idx: "+idx);
				List<WebElement> li = shared_functions.getElementsWhenVisible(By.cssSelector("#CMSIpPatSpecWin #ipSpecGrid .x-grid3-scroller .x-grid3-row"));
				List<WebElement> li2 = shared_functions.getElementsInsideParentWebElementWhenVisible(li.get(idx), By.cssSelector(".x-grid3-cell-inner.x-grid3-col-specName"));
				li2.get(0).click();
				shared_functions.getElementWhenClickable(By.cssSelector("#CMSIpPatSpecWin #CMSIpPatSpecBtn button")).click();
		}else {
			System.out.println("There is no Select Patient Specialty popup window");
		}
	}
	public void selectSpecialty(String spec, String clinic) {
		driver.switchTo().defaultContent();
		String xp = "//div[contains(text(),'"+spec+"')]";
		shared_functions.clickElementWhenClickable(By.xpath(xp));
		String xp2 = "//div[contains(text(),'"+clinic+"')]";
		shared_functions.clickElementWhenClickable(By.xpath(xp2));
		String xp3 = "//span[@id='CMSOpPatSpecSelectBtn-btnInnerEl']";
		shared_functions.clickElementWhenClickable(By.xpath(xp3));
	}	
	/*
	 	public void closeAlertPanel_toBeDelete() {
		driver.switchTo().defaultContent();
		Boolean openByDetailBtnOfExistingAlertReminderWindow = (shared_functions.getElementsWhenVisible(By.cssSelector("iframe[name=alertWinPanelDetailModalIFrame]")).size()>0);
		Boolean openByAlertBtn = (shared_functions.getElementsWhenVisible(By.cssSelector("iframe[name=alertWinPanelModalIFrame]")).size()>0);
		if( openByDetailBtnOfExistingAlertReminderWindow ) {
			shared_functions.switchToFrameByString("alertWinPanelDetailModalIFrame");				
		}else if( openByAlertBtn ){
			shared_functions.switchToFrameByString("alertWinPanelModalIFrame");				
		}
		WebElement eAlertPanelCloseBtn = shared_functions.getElementWhenClickable(By.cssSelector("#btnCorpAllergyClose"));
		eAlertPanelCloseBtn.click();
	}

	 */
}
