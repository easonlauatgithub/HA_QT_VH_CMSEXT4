package suite;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URL;
import org.openqa.selenium.remote.RemoteWebDriver;

public class T00S01_WebLogin_2S4Fremote {
	Map<String, String> dict = new HashMap<>();
	WebDriver driver = null;
	@Test
	public void test() throws Exception {
		shared_functions.EnvironmentValue_TestName="T00S01_WebLogin_2S4F";
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
		//PAGE_CMSMainPage cmsMainPage  = new PAGE_CMSMainPage(driver);
		//cmsMainPage.cancelListOfPrescriptionsModifiedByPharmacy();
	}
	public void quitDriverIfExist() throws InterruptedException, IOException{
		System.out.println("quitDriverIfExist");
		driver = shared_functions.driver;
		if(driver!=null){
			shared_functions.driver=null;
			driver.quit();
			System.out.println("quitDriverIfExist-driver is quit");
			shared_functions.sleepForAWhile(1000);
			Thread.sleep(30000);
		}
	}
	public void initDriver() throws IOException, InterruptedException{
		System.out.println("initDriver");
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
		shared_functions.driver = newIEDriverInLoop(options);
		shared_functions.wait = new WebDriverWait(driver, 10);
		System.out.println("initDriver,driver1:"+driver);
		System.out.println("initDriver,driver2:"+shared_functions.driver);
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	public WebDriver newIEDriverInLoop(InternetExplorerOptions options) throws InterruptedException, IOException{
		System.out.println("newIEDriverInLoop");
		Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
        try {
        	//driver = new InternetExplorerDriver(options);
        	URL url = new URL("http://160.66.133.194:4444/wd/hub");
        	driver = new RemoteWebDriver(url, options);
        	String expectedTitle = "WebDriver";
        	Boolean b = (new WebDriverWait(driver, 10)).until(ExpectedConditions.titleIs(expectedTitle));
        	return driver;
        }catch (WebDriverException e) {
        	e.printStackTrace();
        	shared_functions.sleepForAWhile(500);
            return newIEDriverInLoop(options);
        }
	}
	public void loginCMS() throws IOException, InterruptedException{
		System.out.println("loginCMS");
		try {
			driver.get(dict.get("login_url"));
        }catch (WebDriverException e) {
        	e.printStackTrace();
        	initDriver();
        }
		shared_functions.switchWindowID(driver);
		System.out.println("loginCMS-eLogin");
		WebElement eLogin = shared_functions.getElementWhenClickable(By.id("cmsLoginId-inputEl"));
		eLogin.sendKeys(dict.get("login_id"));
		System.out.println("loginCMS-ePwd");
		WebElement ePwd = shared_functions.getElementWhenClickable(By.id("cmsPassword-inputEl"));
		ePwd.sendKeys(shared_functions.decodePwd(dict.get("encrypted_password")));
		System.out.println("loginCMS-eEnter");
		shared_functions.clickElementWhenClickable(By.id("logonBtn"));
	}
	public void clickOverrideLink(){
		//System.out.println("PAGE - error page X 2");
		shared_functions.clickElementWhenClickable(By.id("overridelink"));
		shared_functions.clickElementWhenClickable(By.id("overridelink"));
	}
	public Boolean handleIfExistAlertInformation1_6_0_W_010(){
		/*System.out.println("HANDLE ALERT - Information(1-6-0-W-010) ");
		The system detected that you have another active CMS logon or abnormal logout at "VH_EXT4".
	 	Please close the other CMS session before starting a new session. 
		An audit log entry has been created to document your concurrent logon. 
		Please remember to log off or "time-out" the CMS everytime you leave the workstation.*/
		/*
		System.out.println("HANDLE ALERT - Click Print");
		Boolean isPrintBtnExist = shared_functions.getElementsWhenVisible(By.cssSelector(".x-window-body.x-border-layout-ct>.x-panel.x-border-panel.x-panel-noborder")).size()>0;
		if(isPrintBtnExist) { 
			List<WebElement> liPrint1=shared_functions.getElementsWhenVisible(By.cssSelector(".x-window-body.x-border-layout-ct>.x-panel.x-border-panel.x-panel-noborder"))
			List<WebElement> liPrint2=shared_functions.getElementsInsideParentWebElementWhenVisible(liPrint1.get(0), By.cssSelector(".x-panel-btn-td"));
			List<WebElement> liPrint3=shared_functions.getElementsInsideParentWebElementWhenVisible(liPrint2.get(0), By.cssSelector(".x-btn-text-cui-n"));
			WebElement eBtnPrint =liPrint3.get(0);
			System.out.println("Tag: "+eBtnPrint.getTagName()+", Text: "+eBtnPrint.getText());
			eBtnPrint.click();
		}
		shared_functions.sleepForAWhile(5000);// let printing process
		*/
		//System.out.println("HANDLE ALERT - Click OK");
		String strOKBtn = "div.x-window div.x-frame-ml div.x-panel.x-box-item";
		List<WebElement> liOKBtn = null;
		WebDriverWait w = new WebDriverWait(driver, 5);
		try{
			liOKBtn = w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(strOKBtn)));	
		}catch(TimeoutException e){
			System.out.println("handleIfExistAlertInformation1_6_0_W_010, TimeoutException");
			e.printStackTrace();
		}		
		if(liOKBtn==null) {
			return false;
		}else{
			WebElement eBtnOK = shared_functions.getElementsInsideParentWebElementWhenVisible(liOKBtn.get(4), By.cssSelector("a.x-btn")).get(0);
			eBtnOK.click();
			return true;
		}
	}
	public void selectUserSpecialty(){
		//System.out.println("PAGE - User Specialty");
		//List<WebElement> li_menu_box = shared_functions.getElementsWhenVisible(By.cssSelector("#userSpecGrid>.x-panel-bwrap>.x-panel-body .x-grid3-scroller .x-grid3-row")); //NLT
		List<WebElement> li_menu_box = shared_functions.getElementsWhenVisible(By.cssSelector("#userSpecGrid-body tr.x-grid-row"));
		/*DEBUG
		for(int i =0; i<li_menu_box.size(); i++ ) {
			li_menu_box.get(i).click();
			shared_functions.sleepForAWhile(1);
		}*/
		li_menu_box.get(0).click(); //SELECT 'MED'
		shared_functions.clickElementWhenClickable(By.cssSelector("#CMSUserSpecSelectBtn"));
	}
	public void inputPurposeForITDLogin(Boolean isExistMultipleSessionAlert){
		//System.out.println("HANDLE ALERT - Purpose for ITD Logon");		
		//Boolean isExistAlertEnterPurposeForITDLogon = shared_functions.getElementsWhenVisible(By.cssSelector(".x-window-bwrap .x-window-bl .x-panel-btns.x-panel-btns-center>table td.x-panel-btn-td")).size()>0; //NLT
		String searchPurposeForITDLogonPopUpWindow = "div.x-window input.x-form-field.x-form-text";
		int sizePurposeForITDLogonPopUpWindow = shared_functions.getElementsWhenPresent(By.cssSelector( searchPurposeForITDLogonPopUpWindow )).size();
		Boolean isExistPurposeForITDLogonPopUpWindow = sizePurposeForITDLogonPopUpWindow>0;
		if(isExistPurposeForITDLogonPopUpWindow){
			WebElement eModalInput = shared_functions.getElementsWhenPresent(By.cssSelector( searchPurposeForITDLogonPopUpWindow )).get( sizePurposeForITDLogonPopUpWindow-1 );
			eModalInput.sendKeys(dict.get("login_reason"));
			List<WebElement> liBtn = shared_functions.getElementsWhenPresent(By.cssSelector( "div.x-window div.x-toolbar a.x-btn" ));
			/*DEBUG
			for(int i=0; i<liBtn.size(); i++) {
				Boolean b = liBtn.get(i).isDisplayed();
				System.out.println("b("+i+"): "+b);
			}*/
			WebElement eOkBtn = null;
			if(isExistMultipleSessionAlert) {
				eOkBtn=liBtn.get(4);
			}else {
				eOkBtn=liBtn.get(0);
			}
			eOkBtn.click();
		}
	}
}
