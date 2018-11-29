package suite;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class T00S01_WebLogin_2S4F {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName = "T00S01_WebLogin_2S4F";
		shared_functions.Parameter.put("hospital_code", "VH3");
		shared_functions.Parameter.put("test", "VH3_CMSIT_extjs4");
		dict = shared_functions.sam_gor();
		quitDriverIfExist();
		initDriver();
		loginCMS();
		clickOverrideLink();
		Boolean isExistInformation1_6_0_W_010 = handleIfExistAlertInformation1_6_0_W_010();
		selectUserSpecialty();
		inputPurposeForITDLogin(isExistInformation1_6_0_W_010);
		cancelListOfPrescriptionsModifiedByPharmacy();
	}
	public void cancelListOfPrescriptionsModifiedByPharmacy(){
		driver.switchTo().defaultContent();
		driver.switchTo().frame("230ModalIFrame");
		driver.findElement(By.id("pRmkUserCancelBtn")).click();
	}
	public void quitDriverIfExist() throws InterruptedException, IOException{
		driver = shared_functions.driver;
		if(driver!=null){
			driver.quit();
		}
	}
	public void initDriver() throws IOException, InterruptedException{
		Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
		Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
		Runtime.getRuntime().exec("taskkill /F /IM IEDriver32Server.exe");
		String ieDriverServerFilePath = "C:\\eclipse_neon\\workspace\\lib\\selenium\\3.14\\";
		//System.setProperty("webdriver.ie.driver.loglevel","INFO");     
		//System.setProperty("webdriver.ie.driver.logfile","C:\\Users\\LYS678\\Desktop\\IEServerlog.log");
		//IE64
		//System.setProperty("webdriver.ie.driver",ieDriverServerFilePath+"IEDriverServer.exe");
		//InternetExplorerOptions options = new InternetExplorerOptions().requireWindowFocus();
		//IE32
		System.setProperty("webdriver.ie.driver",ieDriverServerFilePath+"IEDriver32Server.exe");
		InternetExplorerOptions options = new InternetExplorerOptions();
		driver = newIEDriverInLoop(options);
		shared_functions.driver = driver;
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	public InternetExplorerDriver newIEDriverInLoop(InternetExplorerOptions options) throws InterruptedException, IOException{
		Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
        try {
        	return new InternetExplorerDriver(options);
        } catch (WebDriverException e) {
        	shared_functions.sleepForAWhile(500);
            return newIEDriverInLoop(options);
        }
	}
	public void loginCMS() throws UnsupportedEncodingException{
		//driver.get("about:NoAdd-ons");
		driver.get(dict.get("login_url"));
		shared_functions.switchWindowID(driver);
		//System.out.println("PAGE - logon");
		//driver.findElement(By.id("cmsLoginId")).sendKeys(dict.get("login_id")); //NLT 
		//driver.findElement(By.id("cmsPassword")).sendKeys(dict.get("encrypted_password")); //NLT
		driver.findElement(By.id("cmsLoginId-inputEl")).sendKeys(dict.get("login_id"));
		driver.findElement(By.id("cmsPassword-inputEl")).sendKeys( shared_functions.decodePwd(dict.get("encrypted_password")) );
		driver.findElement(By.id("logonBtn")).click();
	}
	public void clickOverrideLink(){
		//System.out.println("PAGE - error page X 2");
		driver.findElement(By.id("overridelink")).click();
		driver.findElement(By.id("overridelink")).click();
	}
	public Boolean handleIfExistAlertInformation1_6_0_W_010(){
		/*System.out.println("HANDLE ALERT - Information(1-6-0-W-010) ");
		The system detected that you have another active CMS logon or abnormal logout at "VH_EXT4".
	 	Please close the other CMS session before starting a new session. 
		An audit log entry has been created to document your concurrent logon. 
		Please remember to log off or "time-out" the CMS everytime you leave the workstation.*/
		/*
		System.out.println("HANDLE ALERT - Click Print");
		Boolean isPrintBtnExist = driver.findElements(By.cssSelector(".x-window-body.x-border-layout-ct>.x-panel.x-border-panel.x-panel-noborder")).size()>0;
		if(isPrintBtnExist) {
			WebElement eBtnPrint = driver.findElements(By.cssSelector(".x-window-body.x-border-layout-ct>.x-panel.x-border-panel.x-panel-noborder")).get(0).findElements(By.cssSelector(".x-panel-btn-td")).get(0).findElement(By.cssSelector(".x-btn-text-cui-n"));
			System.out.println("Tag: "+eBtnPrint.getTagName()+", Text: "+eBtnPrint.getText());
			eBtnPrint.click();
		}
		shared_functions.sleepForAWhile(5000);// let printing process
		*/
		//System.out.println("HANDLE ALERT - Click OK");
		String OKBtn = "div.x-window div.x-frame-ml div.x-panel.x-box-item";
		Boolean isExistMultipleSessionAlert = driver.findElements(By.cssSelector(OKBtn)).size()>0;
		if(isExistMultipleSessionAlert) {
			WebElement eBtnOK = driver.findElements(By.cssSelector(OKBtn)).get(4).findElement(By.cssSelector("a.x-btn"));
			eBtnOK.click();
			return true;
		}else{return false;}
	}
	public void selectUserSpecialty(){
		//System.out.println("PAGE - User Specialty");
		//List<WebElement> li_menu_box = driver.findElements(By.cssSelector("#userSpecGrid>.x-panel-bwrap>.x-panel-body .x-grid3-scroller .x-grid3-row")); //NLT
		List<WebElement> li_menu_box = driver.findElements(By.cssSelector("#userSpecGrid-body tr.x-grid-row"));
		/*DEBUG
		for(int i =0; i<li_menu_box.size(); i++ ) {
			li_menu_box.get(i).click();
			shared_functions.sleepForAWhile(1);
		}*/
		li_menu_box.get(0).click(); //SELECT 'MED'
		WebElement eSelect = driver.findElement(By.cssSelector("#CMSUserSpecSelectBtn"));
		eSelect.click();
	}
	public void inputPurposeForITDLogin(Boolean isExistMultipleSessionAlert){
		//System.out.println("HANDLE ALERT - Purpose for ITD Logon");		
		//Boolean isExistAlertEnterPurposeForITDLogon = driver.findElements(By.cssSelector(".x-window-bwrap .x-window-bl .x-panel-btns.x-panel-btns-center>table td.x-panel-btn-td")).size()>0; //NLT
		String searchPurposeForITDLogonPopUpWindow = "div.x-window input.x-form-field.x-form-text";
		int sizePurposeForITDLogonPopUpWindow = driver.findElements(By.cssSelector( searchPurposeForITDLogonPopUpWindow )).size();
		Boolean isExistPurposeForITDLogonPopUpWindow = sizePurposeForITDLogonPopUpWindow>0;
		if(isExistPurposeForITDLogonPopUpWindow){
			WebElement eModalInput = driver.findElements(By.cssSelector( searchPurposeForITDLogonPopUpWindow )).get( sizePurposeForITDLogonPopUpWindow-1 );
			eModalInput.sendKeys(dict.get("login_reason"));
			WebElement eOkBtn = null;
			List<WebElement> liBtn = driver.findElements(By.cssSelector( "div.x-window div.x-toolbar a.x-btn" ));
			/*DEBUG
			for(int i=0; i<liBtn.size(); i++) {
				Boolean b = liBtn.get(i).isDisplayed();
				System.out.println("b("+i+"): "+b);
			}*/
			if(isExistMultipleSessionAlert) {
				eOkBtn=liBtn.get(4);
			}else {
				eOkBtn=liBtn.get(0);				
			}
			eOkBtn.click();
		}
	}
}
