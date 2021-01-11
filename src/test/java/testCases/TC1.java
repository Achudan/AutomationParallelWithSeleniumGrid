package testCases;

import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import dataProviderUtility.dataProviderParams;
import masterWrapper.CaptainOfTheShip;
import utilities.AssertionErrorException;
import utilities.ThrowCannonBallException;

@Listeners(utilities.ListenerClass.class)
public class TC1 extends CaptainOfTheShip{


	@dataProviderParams({"sampleMethod1DataWorkbook", "Sheet1"})
	@Test(dataProvider = "dp")
	public void sampleMethod(ITestContext context, Map<String, String> data) {
		
		String URL = MasterProp.getProperty("URL");
		URL = data.get("URL");
		CaptainOfTheShip startApp = null;
		try {
			startApp = new CaptainOfTheShip()
					.startReporting("SampleReport1","1st test")
					.startApplication("chrome", URL)
					.startPage()
					.getEmailandVerify("Homepage", "Front-End")
					.getPasswordandVerify("Homepage", "Front-End")
					.getEmailandVerify("Administrator", "Back-End")
					.getPasswordandVerify("Administrator", "Back-End")
					.endOfTest();
		} catch (ThrowCannonBallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AssertionErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.setAttribute("webdriver", startApp.driver);
		context.setAttribute("extentLog", startApp.extentLog);
		context.setAttribute("test", startApp.test);
	}

	@Test(alwaysRun = false) 
	public void sampleMethod2(ITestContext context) {
//		String URL = MasterProp.getProperty("URL");
		String URL = "https://phptravels.com/demo";
		CaptainOfTheShip startApp = null;
		try {
			startApp = new CaptainOfTheShip()
					.startReporting("SampleReport2","2nd test")
					.startApplication("chrome", URL)
					.startPage()
					.getEmailandVerify("Homepage", "Front-End")
					.getPasswordandVerify("Homepage", "Front-End")
					.getEmailandVerify("Administrator", "Back-End")
					.getPasswordandVerify("Administrator", "Back-End")
					.endOfTest();
		} catch (ThrowCannonBallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AssertionErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		context.setAttribute("webdriver", startApp.driver);
		context.setAttribute("extentLog", startApp.extentLog);
		context.setAttribute("test", startApp.test);
		}

	@Test(alwaysRun = false) 
	public void sampleMethod3(ITestContext context) {
		CaptainOfTheShip startApp = null;
		try {
			startApp = new CaptainOfTheShip()
					.startReporting("SampleReport3","3rd test")
					.startApplication("chrome", "https://phptravels.com/demo")
					.startPage()
					.getEmailandVerify("Homepage", "Front-End")
					.getPasswordandVerify("Homepage", "Front-End")
					.getEmailandVerify("Administrator", "Back-End")
					.getPasswordandVerify("Administrator", "Back-End")
					.endOfTest();
		} catch (ThrowCannonBallException e) {
			e.printStackTrace();
		} catch (AssertionErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			System.out.println("finally");
		context.setAttribute("webdriver", startApp.driver);
		context.setAttribute("extentLog", startApp.extentLog);
		context.setAttribute("test", startApp.test);
		}
		
	}

}
