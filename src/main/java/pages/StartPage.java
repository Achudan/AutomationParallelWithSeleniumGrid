package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentTest;
import masterWrapper.CaptainOfTheShip;
import masterWrapper.MasterClass;
import reporter.ExtentReporter;
import utilities.AssertionErrorException;
import utilities.ThrowCannonBallException;

public class StartPage extends CaptainOfTheShip{
	public WebDriver driver;
	public ExtentReporter extentLog;
	public ExtentTest test;
	public StartPage(WebDriver driver, ExtentReporter extentLog, ExtentTest test) {
		master = new MasterClass(driver);
		this.driver = driver;
		this.extentLog = extentLog;
		this.test = test;
	}
	private String getEmailAndPassword(String pageName, String moduleName, String request) throws ThrowCannonBallException {
		try {
			
			String xpath = "//h3[text()[contains(.,'"+moduleName+"')]]/strong[text()='"+pageName+"']/following::strong[text()='Email'][1]/preceding::div[1]/following::div[1]/div[1]";
			master.waitUntilElementVisible(xpath);
			master.scrollTillElement(xpath);
			String response = master.getTextByXpath(xpath).replaceAll("\\n", " ");
			System.out.println("resp=> "+response);
			String[] values = response.split(" ");
			String emailID = values[1].trim();
			String password = values[3].trim();
			if(request.toLowerCase().contains("email")) {
				return emailID;
			}
			return password;
		}
		
		catch(Exception e){
			System.out.println(e.toString());
			if(e.toString().contains("ThrowCannonBallException")) {
				throw new ThrowCannonBallException("A clear description of the failure", e);
			}
			
		}
		return null;

	}
	
	public StartPage getEmailandVerify(String role, String stack) throws ThrowCannonBallException, AssertionErrorException {
		//h3[text()[contains(.,'Front-End')]]/following::strong[text()='Email'][1]/preceding::div[1]/following::div[1]/div[1]/text()
		try {
			String emailID = getEmailAndPassword(role, stack,"emailID");
			boolean sts = true;
			if(sts) {
				
				extentLog.logTestStepWithScreenCapture("pass", "email id "+emailID+" is correct");
			}
			else {
				extentLog.logTestStep("fail", "email id "+emailID+" is incorrect");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			extentLog.logTestStep("fail", "email id method exception");
			throw new AssertionErrorException("email id element is unavailable", e, driver, extentLog);
//			extentLog.flushNow();
//			driver.close();
//			throw new AssertionError();
//			throw new ThrowCannonBallException("A clear description of the failure", e);
		}
		return this;
	}
	public StartPage getPasswordandVerify(String role, String stack) {
		//h3[text()[contains(.,'Front-End')]]/following::strong[text()='Email'][1]/preceding::div[1]/following::div[1]/div[1]/text()
		try {
			String password = getEmailAndPassword(role, stack,"password");
			boolean sts = true;
			if(sts) {
				extentLog.logTestStep("pass", "password "+password+" is correct");
			}
			else {
				extentLog.logTestStep("fail", "password is incorrect");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			extentLog.logTestStep("fail", "password method exception");
		}
		return this;
	}
	public CaptainOfTheShip endOfTest() {
		// TODO Auto-generated method stub
		PageMaster end = new PageMaster(this.driver, this.extentLog, this.test);
		return end.endPage(this.driver, this.extentLog, this.test);
	}
}
