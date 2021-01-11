package reporter;

import java.io.File;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import masterWrapper.CaptainOfTheShip;

public class ExtentReporter extends CaptainOfTheShip{
	public ExtentReports extent;
	public ExtentTest test;
	public String testCaseName = "";
	public static int testImageNumber = 0;
	public WebDriver driver;
	
//	public ExtentReporter(WebDriver driver) {
//		this.driver = driver;
//	}
	
	public ExtentTest startTest(String tcName, String desc)
	{
		if(!testCaseName.equals(tcName)) {
			testCaseName = tcName;
			extent = new ExtentReports();
			ExtentSparkReporter spark = new ExtentSparkReporter(System.getProperty("user.dir")+"\\Reports\\"+testCaseName+".html");
			extent.attachReporter(spark);
		}

		test = extent.createTest(desc);
		return test;
	}
	public String capture(WebDriver driver,String screenShotName) throws IOException
    {
        TakesScreenshot ts = (TakesScreenshot)driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String dest = System.getProperty("user.dir") +"\\Reports\\"+screenShotName+".png";
        File destination = new File(dest);
        FileUtils.copyFile(source, destination);        
                     
        return dest;
    }
	public void flushNow()
	{
		extent.flush();
//		testImageNumber =0;
		System.out.println(testCaseName);
	}
	
	public void logTestStep(String decision, String description)
	{
		
		try {
			if(decision.equalsIgnoreCase("pass")) {
				test.log(Status.PASS, description);
			}
			else if(decision.equalsIgnoreCase("fail")) {
				test.log(Status.FAIL, description);
			}
			else{
				test.log(Status.INFO, description);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			test.log(Status.WARNING,"error occurred");
		}
	}
	public void logTestStepWithScreenCapture(String decision, String description)
	{
		String location="";
		try {
			location = capture(driver, "image"+(++testImageNumber));
			if(decision.equalsIgnoreCase("pass")) {
				test.pass(description, MediaEntityBuilder.createScreenCaptureFromPath(location,description).build());
			}
			else if(decision.equalsIgnoreCase("fail")) {
				test.fail(description, MediaEntityBuilder.createScreenCaptureFromPath(location,description).build());
			}
			else{
				test.info(description, MediaEntityBuilder.createScreenCaptureFromPath(location,description).build());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			test.skip(description, MediaEntityBuilder.createScreenCaptureFromPath(location,description).build());
		}
	}
	public void setDriver(WebDriver driver) {
		// TODO Auto-generated method stub
		this.driver = driver;
	}
	public void setTest(ExtentTest test) {
		// TODO Auto-generated method stub
		this.test = test;
	}
}
