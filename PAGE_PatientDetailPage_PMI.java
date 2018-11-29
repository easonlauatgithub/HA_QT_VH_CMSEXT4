package suite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

class PAGE_PatientDetailPage_PMI {
	WebDriver driver = null;
	int passedCounter = 0;
	public PAGE_PatientDetailPage_PMI(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}
	public void switchToIframe(){
		driver.switchTo().defaultContent();
		driver.switchTo().frame("96Panel");			
	}
	public void clickTab(int idxTabName) {
		//idxTabName: 0 PMI Enq, 1 NOK
		switchToIframe();
		List<WebElement> liTab = driver.findElements(By.cssSelector("body>div.x-panel>div.x-tab-bar a"));
		liTab.get( idxTabName ).click(); 
	}
	public void clickCollapsibleTab(String cExpandOrCollapse, int idxCollapsibleTab) {
		// cExpandOrCollapse: c Collapse, e Expand
		// idxCollapsibleTab: 0 demographic, 1 address
		switchToIframe();
		List<WebElement> liExpandable = driver.findElements(By.cssSelector("img.x-tool-img.x-tool-expand-bottom"));
		List<WebElement> liCollapsible = driver.findElements(By.cssSelector("img.x-tool-img.x-tool-collapse-top"));
		switch( cExpandOrCollapse.toLowerCase().charAt(0) ){
			case 'c':
				liCollapsible.get( idxCollapsibleTab ).click();
				break;
			case 'e':
				liExpandable.get( idxCollapsibleTab ).click();
				break;
			default:
				System.out.println("No Collapsible Tab is expanded or collapsed");
				break;
		}
	}
	public void clickTab_PMIEnq() {clickTab(0);}
	public void clickTab_NOK() {clickTab(1);}
	public void expandPatientDemographic() {clickCollapsibleTab("e",0);}
	public void collapsePatientDemographic() {clickCollapsibleTab("c",0);}
	public void expandPatientAddress() {clickCollapsibleTab("e",1);}
	public void collapsePatientAddress() {clickCollapsibleTab("c",1);}		
	public String getPatientAddress(){
		switchToIframe();
		List<WebElement> liPatientAddressItem = driver.findElements(By.cssSelector("div[id^=patientAddressPanel] table.x-field"));
		WebElement eBuilding = liPatientAddressItem.get(3).findElement(By.cssSelector("td:nth-child(2) textarea"));
		return eBuilding.getText();			
	}		
	public Map getMapOfPatientDemographicData() {
		switchToIframe();
		List<WebElement> liPatientDemographicItem = driver.findElements(By.cssSelector("div[id^=patientDemoPanel] table.x-field"));
		/*for(int i=0; i<liPatientDemographicItem.size(); i++) {
			WebElement ePatientDemographic = liPatientDemographicItem.get(i).findElement(By.cssSelector("td:nth-child(2) input"));
			System.out.println(i+", ePatientDemographic: "+ePatientDemographic.getAttribute("value"));
		}*/
		Map<String,WebElement> mapOfPatientDemographicData =  new HashMap<>();
		mapOfPatientDemographicData.put( "hkid" , liPatientDemographicItem.get(0).findElement(By.cssSelector("td:nth-child(2) input")) );
		mapOfPatientDemographicData.put( "chi_name" , liPatientDemographicItem.get(1).findElement(By.cssSelector("td:nth-child(2) input")) );
		mapOfPatientDemographicData.put( "case_no" , liPatientDemographicItem.get(2).findElement(By.cssSelector("td:nth-child(2) input")) );
		mapOfPatientDemographicData.put( "eng_name" , liPatientDemographicItem.get(3).findElement(By.cssSelector("td:nth-child(2) input")) );
		mapOfPatientDemographicData.put( "dob" , liPatientDemographicItem.get(5).findElement(By.cssSelector("td:nth-child(2) input")) );
		mapOfPatientDemographicData.put( "age" , liPatientDemographicItem.get(6).findElement(By.cssSelector("td:nth-child(2) input")) );
		mapOfPatientDemographicData.put( "sex" , liPatientDemographicItem.get(8).findElement(By.cssSelector("td:nth-child(2) input")) );
		return mapOfPatientDemographicData;
	}
	public String getNokName() {
		return driver.findElement(By.cssSelector("#id_name_nok")).getAttribute("value");
	}
	public String getMajorNok() {
		return  driver.findElement(By.cssSelector("#id_major_nok")).getAttribute("value");
	}
	public ArrayList<ArrayList<String>> getTableEpisodeCaseList(){
		switchToIframe();
		String str_li1 = "div.x-panel-body.x-grid-with-row-lines table.x-grid-table tr.x-grid-row";
		ArrayList<ArrayList<String>> table = new ArrayList<>();
		List<WebElement> liRows = driver.findElements(By.cssSelector(str_li1));
        //System.out.println("liRows - " +rows.size());
        for(int i=0; i<liRows.size(); i++) {
    		String str_li2 = "table.x-grid-table tr.x-grid-data-row td.x-grid-cell div.x-grid-cell-inner";
            List<WebElement> liCols = liRows.get(i).findElements(By.cssSelector(str_li2));
            //System.out.println("liCols - " +liCols.size());
            ArrayList<String> liRowString = new ArrayList<>();
            for(int j=0; j<liCols.size(); j++) {
            	liRowString.add(liCols.get(j).getText().toString());
            	//System.out.println( i+","+j+": "+liCols.get(j).getText() );
            }
            table.add(liRowString);
        }
        return table;
	}
	

}