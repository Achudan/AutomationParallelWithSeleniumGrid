package testCases;

import org.testng.ITestContext;
import org.testng.annotations.Test;
import masterWrapper.CaptainOfTheShip;
import utilities.AssertionErrorException;
import utilities.ThrowCannonBallException;

public class TC1 extends CaptainOfTheShip{
	@Test
	public void sampleMethod(ITestContext context) {
		
		String URL = MasterProp.getProperty("URL");
		
		CaptainOfTheShip startApp = null;
		try {
			startApp = new CaptainOfTheShip()
					.startReporting("SampleReport","1st test")
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

	@Test(alwaysRun = true) public void sampleMethod2(ITestContext context) {
		String URL = MasterProp.getProperty("URL");
		CaptainOfTheShip startApp = null;
		try {
			startApp = new CaptainOfTheShip()
					.startReporting("SampleReport2","2nd test")
					.startApplication("edge", URL)
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

	@Test public void sampleMethod3(ITestContext context) {
		CaptainOfTheShip startApp = null;
		try {
			startApp = new CaptainOfTheShip()
					.startReporting("SampleReport2","3rd test")
					.startApplication("edge", "https://phptravels.com/demo")
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
