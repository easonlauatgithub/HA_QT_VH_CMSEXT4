package suite;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import suite.T01S03_corp_allergy.PAGE_AlertPanel;

public class PAGE_PatientDetailPage_PatientSpecificFunction {
	WebDriver driver = null;
	T01S03_corp_allergy instanceT01S03_corp_allergy =  new T01S03_corp_allergy();
	public PAGE_PatientDetailPage_PatientSpecificFunction(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}
	public void closeExistingAlertReminderWindow() throws InterruptedException {
System.out.println("PSF-closeExistingAlertReminderWindow-START");
		driver.switchTo().defaultContent();
		Boolean isExistAlertReminderWindow = driver.findElements(By.cssSelector("iframe[name=alertWinPanelModalIFrame]")).size()>0;
		System.out.println("PSF-closeExistingAlertReminderWindow-isExistAlertReminderWindow:"+isExistAlertReminderWindow);
		//exist, Clickkkkkk until close
		while(isExistAlertReminderWindow){
System.out.println("PSF-closeExistingAlertReminderWindow-alert window exist");			
			driver.switchTo().frame("alertWinPanelModalIFrame");
			//Get Close Button
			List<WebElement> liCloseBtn = null;
			try {
				liCloseBtn = driver.findElements(By.cssSelector("#btnCorpAllergyClose"));				
			}catch(JavascriptException e) {
System.out.println("PSF-closeExistingAlertReminderWindow-JavascriptException, sleep");
				shared_functions.sleepForAWhile(5000);
				liCloseBtn = driver.findElements(By.cssSelector("#btnCorpAllergyClose"));
			}
			//click Close
			if(liCloseBtn.size()>0){
				System.out.println("PSF-closeExistingAlertReminderWindow-closeBtn click:"+liCloseBtn.size());
				liCloseBtn.get(0).click();
				shared_functions.sleepForAWhile(500);
			}
			//Check closed or not
			driver.switchTo().defaultContent();
			isExistAlertReminderWindow = driver.findElements(By.cssSelector("iframe[name=alertWinPanelModalIFrame]")).size()>0;
System.out.println("PSF-closeExistingAlertReminderWindow-after click close, still exist:"+isExistAlertReminderWindow);				
		}
System.out.println("PSF-closeExistingAlertReminderWindow-END");
	}
	/*
	public void closeExistingAlertReminderWindow() throws InterruptedException {
		driver.switchTo().defaultContent();
		Boolean isExistAlertReminderWindow = driver.findElements(By.cssSelector("iframe[name=alertWinPanelModalIFrame]")).size()>0;
		if(isExistAlertReminderWindow) {
			System.out.print("PSF-closeExistingAlertReminderWindow, ");
			shared_functions.sleepForAWhile(5000);
			driver.switchTo().frame("alertWinPanelModalIFrame");
			shared_functions.clickBtnByCssWithException("#btnCorpAllergyClose");
		}
	}
	*/
	/* origin
	public void closeExistingAlertReminderWindow() throws InterruptedException {
		driver.switchTo().defaultContent();
		if(driver.findElements(By.cssSelector("iframe[name=alertWinPanelModalIFrame]")).size()!=0) {
			driver.switchTo().frame("alertWinPanelModalIFrame");
			try {
				System.out.println("PAGE - PSF - closeExistingAlertReminderWindow : "+driver.findElements(By.cssSelector("#btnCorpAllergyClose")).size());				
			}catch(JavascriptException e) {
				System.out.println("PAGE - PSF - JavascriptException exist, sleep 5s");
				try {
					shared_functions.sleepForAWhile(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println("PAGE - PSF - after sleep closeExistingAlertReminderWindow : "+driver.findElements(By.cssSelector("#btnCorpAllergyClose")).size());
			}
			shared_functions.sleepForAWhile(5000);
			if( driver.findElements(By.cssSelector("#btnCorpAllergyClose")).size()!=0 ) {
				System.out.println("PAGE - PSF - closeExistingAlertReminderWindow - close");
				WebElement eCloseAlertReminderWindow = driver.findElement(By.cssSelector("#btnCorpAllergyClose"));
				eCloseAlertReminderWindow.click();
			}
		}
	}
	*/
	public void closeDeadWarning() {
		driver.switchTo().defaultContent();
		String xpDesc = "//textarea[contains(text(), 'Patient has already been recorded dead on')]";
		if( driver.findElements(By.xpath(xpDesc)).size()!=0 ) {
			String xpOk = "//span[contains(text(), 'K')]//u[contains(text(), 'O')]";
			if( driver.findElements(By.xpath(xpOk)).size()!=0 ) {
				driver.findElements(By.xpath(xpOk)).get(0).click();
			}
		}
	}
	public WebElement getAlertBtn() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("spioTabPanel");
		//WebElement eAlertPanel = driver.findElement(By.cssSelector("#spioPanel #alertPanel #alertBtn button")); //NLT
		WebElement eAlertBtn = driver.findElement(By.cssSelector("#alertBtn")); // #alertBtn-frame1Table
		return eAlertBtn;
	}
	public Boolean checkAlertBtn(String check_color) {
		Boolean retval = false;
		String color_found = "";
		WebElement eBtnAlert = this.getAlertBtn();
		String clsNameOfBtnAlert = eBtnAlert.getAttribute("class"); 
		if( clsNameOfBtnAlert.contains( "red-btn" )   ) { 
			color_found = color_found+"red";
		}else if( clsNameOfBtnAlert.contains( "grey-btn" )   ) { 
			color_found = color_found+"grey";
		}else if( clsNameOfBtnAlert.contains( "green-btn" )   ) { 
			color_found = color_found+"green";
		}
		if( color_found.contains( check_color )  ) {
			retval = true;
		}
		return retval;
	}
	public PAGE_AlertPanel clickAlertBtn() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("spioTabPanel");
		//WebElement eAlertPanel = driver.findElement(By.cssSelector("#spioPanel #alertPanel #alertBtn button")); //NLT
		WebElement eAlertPanel = driver.findElement(By.cssSelector("#alertBtn"));
		eAlertPanel.click();
		return instanceT01S03_corp_allergy.new PAGE_AlertPanel(driver);
	}
	public PAGE_AlertPanel clickDetailBtnOfExistingAlertReminderWindow() {
		driver.switchTo().defaultContent();
		if(driver.findElements(By.cssSelector("iframe[name=alertWinPanelModalIFrame]")).size()!=0) {
			driver.switchTo().frame("alertWinPanelModalIFrame");
			if( driver.findElements(By.cssSelector("#btnCorpAllergyDetail")).size()!=0 ) {
				WebElement eAlertDetail = driver.findElement(By.cssSelector("#btnCorpAllergyDetail"));
				eAlertDetail.click();
			}
		}
		return instanceT01S03_corp_allergy.new PAGE_AlertPanel(driver);
	}



	
}