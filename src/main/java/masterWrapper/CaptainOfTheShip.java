package masterWrapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.aventstack.extentreports.ExtentTest;

import dataBaseConnection.AtlasMongoDBConnection;
import dataProviderUtility.DataProviderBase;
import dataProviderUtility.dataProviderParams;
import pages.PageMaster;
import propertyReader.PropertyFileController;
import reporter.ExtentReporter;

public class CaptainOfTheShip{

	public WebDriver driver;
	public MasterClass master;
	public ExtentReporter extentLog;
	public ExtentTest test;
	public PropertyFileController prop;
	public static Properties MasterProp;
	public static ArrayList<WebDriver> listOfDrivers = new ArrayList<WebDriver>();
	public static ArrayList<ExtentReporter> listOfExtentLogs = new ArrayList<ExtentReporter>();
	public static AtlasMongoDBConnection mongoDBConnection;
	public static String parallelMode;
	public static int classCount;
	
	public void setDriverAndExtentLog(WebDriver driver, ExtentReporter extentLog, ExtentTest test) {
		System.out.println("setting driver "+ driver.getClass());
		this.driver = driver;
		this.extentLog = extentLog;
		this.test = test;
	}
	public WebDriver getDriver() {
		return this.driver;
	}
	public PageMaster startApplication(String browserName, String URL) {
		DesiredCapabilities dr=null;		
		String isGrid = MasterProp.getProperty("runInGrid");
		try {
			if(browserName.equalsIgnoreCase("chrome")) {
				System.out.println("The thread ID for Chrome is "+ Thread.currentThread().getId());
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/Drivers/chromedriver.exe");
				dr=new DesiredCapabilities();
				dr.setBrowserName("chrome");
				//				dr.setPlatform(Platform.WINDOWS);

				ChromeOptions options = new ChromeOptions();
				options.addArguments("--no-sandbox");
				options.addArguments("--aggressive-cache-discard");
				options.merge(dr);

				if(isGrid.equalsIgnoreCase("true")) {
					driver=new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
				}
				else {
					ChromeDriverService service= new ChromeDriverService.Builder()
					        .usingDriverExecutable(new File(System.getProperty("user.dir")+"/Drivers/chromedriver.exe"))
					        .usingAnyFreePort()
					        .build();
					    service.start();
					driver = new ChromeDriver(service,options);
				}

			}
			else if(browserName.equalsIgnoreCase("edge")) {
				System.out.println("The thread ID for edge is "+ Thread.currentThread().getId());
				System.setProperty("webdriver.edge.driver", "./Drivers/msedgedriver.exe");
				dr=new DesiredCapabilities();
				dr.setBrowserName("MicrosoftEdge");
				dr.setPlatform(Platform.WINDOWS);

				EdgeOptions options = new EdgeOptions();
				options.merge(dr);

				if(isGrid.equalsIgnoreCase("true")) {
					driver=new RemoteWebDriver(new URL("http://localhost:5551/wd/hub"), options);
				}
				else {
					driver = new EdgeDriver(options);
				}
			}
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.get(URL);
			extentLog.setDriver(driver);
			extentLog.setTest(test);
			setDriverAndExtentLog(driver, extentLog, test);
		} 
		
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(e.getCause().toString().contains("ConnectException")) {
				driver.quit();
				startApplication(browserName, URL);
			}
		}
		return new PageMaster(this.driver, this.extentLog, this.test);

	}

	@DataProvider(name = "dp")
	public Object[][] getData(Method m) throws IOException{

		String[] workbookParams = m.getAnnotation(dataProviderParams.class).value();
		String fileName = workbookParams[0];
		String sheetName = workbookParams[1];
		String fileLocation = System.getProperty("user.dir")+"\\src\\main\\resources\\utilities\\dataProviderFiles\\"+fileName+".xlsx";

		DataProviderBase dataProviderBase = new DataProviderBase();
		Object[][] dataInHashMap = dataProviderBase.getDataForDataProvider(fileLocation, sheetName);

		return dataInHashMap;
	}
	@BeforeSuite
	public void instantiate() {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
			Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe");
			Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
			Runtime.getRuntime().exec("taskkill /F /IM msedge.exe");

			//creating object for property file
			prop = new PropertyFileController();
			MasterProp = prop.readPropertiesFile("masterProperty");

			parallelMode = prop.readPropertiesFile("listernerAndTestngXmlProp", "parallelMode");
			classCount =Integer.valueOf(prop.readPropertiesFile("listernerAndTestngXmlProp","classesCount"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestContext context) {
		//		extent.endTest(test);
		System.out.println("In after method");
		System.out.println("In after method==> "+context.getAttribute("webdriver"));
		System.out.println("In after method==> "+context.getAttribute("extentLog"));
		System.out.println("In after method==> "+context.getAttribute("test"));
		try {
			if(context.getAttribute("extentLog") != null) {
				ExtentReporter extentLogger = (ExtentReporter) context.getAttribute("extentLog");
				listOfExtentLogs.add(extentLogger);
//				extentLogger.flushNow();
			}
			if(context.getAttribute("webdriver") != null) {
				WebDriver dr = (WebDriver) context.getAttribute("webdriver");
				System.out.println("Before closing..............==> "+dr);
				dr.close();
				listOfDrivers.add(dr);
//				dr.close();
				System.out.println("After closing..............==> "+dr);
			}

		} catch(NoSuchSessionException nsse) {
			System.out.println("driver expired");
//			nsse.printStackTrace();
		}catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		WebDriver driver = getDriver();
		//		driver.close();
		//		driver.quit();
	}
	@AfterClass
	public void killAll(ITestContext context) {
		try {
			
			System.out.println("context in after classs "+context.getCurrentXmlTest().getClasses());
			System.out.println("in after class");
//			for( ExtentReporter e: listOfExtentLogs) {
//				e.flushNow();
//			}
//			for(WebDriver d: listOfDrivers) {
//				d.quit();
//			}
			System.out.println("parallel mode====> "+parallelMode);
			
			if(classCount<2) {
				System.out.println("in after class flushing reports");
				for( ExtentReporter e: listOfExtentLogs) {
					e.flushNow();
				}
				
				for(WebDriver d: listOfDrivers) {
					System.out.println(d);
					d.quit();
				}
				
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
				Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe");
				Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
				Runtime.getRuntime().exec("taskkill /F /IM msedge.exe");
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@AfterTest
	public void killAfterTest(ITestContext context) {
		try {
			if(classCount>1) {
				System.out.println("in after test flushing reports");
				for( ExtentReporter e: listOfExtentLogs) {
					e.flushNow();
				}
				for(WebDriver d: listOfDrivers) {
					System.out.println(d);
					d.quit();
				}
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
				Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe");
				Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
				Runtime.getRuntime().exec("taskkill /F /IM msedge.exe");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CaptainOfTheShip startReporting(String testCaseName, String testCaseDescription) {
		// TODO Auto-generated method stub
		extentLog = new ExtentReporter();
		ExtentTest test = extentLog.startTest(testCaseName,testCaseDescription);
		this.test = test;
		return this;
	}

}
