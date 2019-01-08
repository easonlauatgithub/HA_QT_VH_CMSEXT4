package suite;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
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
		Boolean isExistAlertReminderWindow = true;
		WebDriverWait w = new WebDriverWait(driver, 5);
		while(isExistAlertReminderWindow){//exist, Clickkkkkk until close
			driver.switchTo().defaultContent();
			List<WebElement> li =shared_functions.checkAndGetElementsWhenVisible(By.cssSelector("#alertWinPanelCMSModalWin"));
			if(li!=null){
				try{
					w.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("alertWinPanelModalIFrame"));
					shared_functions.clickElementWhenClickable(By.cssSelector("#btnCorpAllergyClose")); //click Close Button
				}catch(TimeoutException ex){
					System.out.println("closeExistingAlertReminderWindow, TimeoutException");
					ex.printStackTrace();
					isExistAlertReminderWindow = false;
				}catch(StaleElementReferenceException ex){
					System.out.println("closeExistingAlertReminderWindow, StaleElementReferenceException");
					ex.printStackTrace();
					isExistAlertReminderWindow=true;
				}catch(JavascriptException ex){
					System.out.println("closeExistingAlertReminderWindow, JavascriptException");
					ex.printStackTrace();
					isExistAlertReminderWindow=true;
				}catch(ElementNotInteractableException ex){
					System.out.println("closeExistingAlertReminderWindow, ElementNotInteractableException");
					ex.printStackTrace();
					isExistAlertReminderWindow=true;				
				}
			}
		}
	}
	/*
	public void closeExistingAlertReminderWindow_TBD() throws InterruptedException {
		Boolean isExistAlertReminderWindow = true;
		while(isExistAlertReminderWindow){//exist, Clickkkkkk until close
			driver.switchTo().defaultContent();
			//shared_functions.sleepForAWhile(500);
			List<WebElement> liExistAlertReminderWindow = null;
			try {
				WebDriverWait w = new WebDriverWait(driver, 5);
				shared_functions.Hardcode(); //below have StaleElementReferenceException
				liExistAlertReminderWindow = w.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("iframe[name=alertWinPanelModalIFrame]")));
			}catch(TimeoutException ex){
				ex.printStackTrace();
			}
			isExistAlertReminderWindow = (liExistAlertReminderWindow!=null);
System.out.println("PSF-closeExistingAlertReminderWindow-isExistAlertReminderWindow:"+isExistAlertReminderWindow);
			if(isExistAlertReminderWindow){
				try {
					shared_functions.switchToFrameByString("alertWinPanelModalIFrame");
					//Get Close Button
					List<WebElement> liCloseBtn = shared_functions.getElementsWhenVisible(By.cssSelector("#btnCorpAllergyClose"));				
					if(liCloseBtn!=null){
						liCloseBtn.get(0).click();//click Close
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		}
	}*/
	public void closeDeadWarning() {
		driver.switchTo().defaultContent();
		String xpDesc = "//textarea[contains(text(), 'Patient has already been recorded dead on')]";
		if( shared_functions.getElementsWhenVisible(By.xpath(xpDesc))!=null ) {
			String xpOk = "//span[contains(text(), 'K')]//u[contains(text(), 'O')]";
			if( shared_functions.getElementsWhenVisible(By.xpath(xpOk))!=null ) {
				shared_functions.getElementsWhenVisible(By.xpath(xpOk)).get(0).click();
			}
		}
	}
	public WebElement getAlertBtn() {
		driver.switchTo().defaultContent();
		shared_functions.switchToFrameByString("spioTabPanel");
		//WebElement eAlertPanel = shared_functions.getElementWhenClickable(By.cssSelector("#spioPanel #alertPanel #alertBtn button")); //NLT
		WebElement eAlertBtn = shared_functions.getElementWhenClickable(By.cssSelector("#alertBtn")); // #alertBtn-frame1Table
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
		shared_functions.switchToFrameByString("spioTabPanel");
		//WebElement eAlertPanel = shared_functions.getElementWhenClickable(By.cssSelector("#spioPanel #alertPanel #alertBtn button")); //NLT
		WebElement eAlertPanel = shared_functions.getElementWhenClickable(By.cssSelector("#alertBtn"));
		eAlertPanel.click();
		return instanceT01S03_corp_allergy.new PAGE_AlertPanel(driver);
	}
	public PAGE_AlertPanel clickDetailBtnOfExistingAlertReminderWindow() {
		driver.switchTo().defaultContent();
		if(shared_functions.getElementsWhenVisible(By.cssSelector("iframe[name=alertWinPanelModalIFrame]"))!=null) {
			shared_functions.switchToFrameByString("alertWinPanelModalIFrame");
			if( shared_functions.getElementsWhenVisible(By.cssSelector("#btnCorpAllergyDetail"))!=null ) {
				WebElement eAlertDetail = shared_functions.getElementWhenClickable(By.cssSelector("#btnCorpAllergyDetail"));
				eAlertDetail.click();
			}
		}
		return instanceT01S03_corp_allergy.new PAGE_AlertPanel(driver);
	}



	
}
