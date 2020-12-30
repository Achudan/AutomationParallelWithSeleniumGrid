package pages;

import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentTest;
import masterWrapper.CaptainOfTheShip;
import masterWrapper.MasterClass;
import reporter.ExtentReporter;

public class PageMaster extends CaptainOfTheShip{

	public WebDriver driver;
	public ExtentReporter extentLog;
	public ExtentTest test;
	
	public PageMaster(WebDriver driver, ExtentReporter extentLog, ExtentTest test) {
		this.driver = driver;
		this.extentLog = extentLog;
		this.test = test;
	}
	
	public StartPage startPage() {
		master = new MasterClass(driver);
		return new StartPage(this.driver, this.extentLog, this.test);
	}
	
	public CaptainOfTheShip endPage(WebDriver driver, ExtentReporter extentLog, ExtentTest test) {
		setDriverAndExtentLog(driver, extentLog, test);
//		driver.close();
//		driver.quit();
		
		return this;
	}
	
}
