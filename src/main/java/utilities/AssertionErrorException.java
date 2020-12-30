package utilities;

import org.openqa.selenium.WebDriver;

import reporter.ExtentReporter;

public class AssertionErrorException extends Exception{
	
	public AssertionErrorException(String message) {
        super(message);
    }
	public AssertionErrorException(String message, Throwable cause, WebDriver driver, ExtentReporter extentLog) {
        super(message, cause);
        extentLog.flushNow();
        driver.close();
//        driver.quit();
        throw new AssertionError();
    }
	
}
