package utilities;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import dataBaseConnection.AtlasMongoDBConnection;
import masterWrapper.CaptainOfTheShip;

public class ListenerClass extends CaptainOfTheShip implements ITestListener{
	
	public void onTestStart(ITestResult result) {

		String testCaseName = result.getInstanceName().replace(".", "_").trim();
		String status = String.valueOf(result.getStatus());
		String testMethodName = result.getName().trim();
		
		System.out.println("testCaseName" +testCaseName);
		System.out.println("status" +status);
		System.out.println("testMethodName" +testMethodName);
		
		try {
			mongoDBConnection.addTestCaseDataToSuiteDocument(testCaseName,testMethodName,status);
		} catch (NullPointerException npe) {
			// TODO Auto-generated catch block
			System.out.println("Mongo not connected");
		}
	}
	
	public void onTestSuccess(ITestResult result) {
		String testCaseName = result.getInstanceName().replace(".", "_").trim();
		String status = String.valueOf(result.getStatus());
		String testMethodName = result.getName().trim();
		
		System.out.println("testCaseName" +testCaseName);
		System.out.println("After method status*******************************************************>>>>>>> " +status);
		System.out.println("testMethodName" +testMethodName);
		
		try {
			mongoDBConnection.addTestCaseDataToSuiteDocument(testCaseName,testMethodName,status);
		} catch (NullPointerException npe) {
			// TODO Auto-generated catch block
			System.out.println("Mongo not connected");
		}
	}
	
	public void onTestFailure(ITestResult result) {
		String testCaseName = result.getInstanceName().replace(".", "_").trim();
		String status = String.valueOf(result.getStatus());
		String testMethodName = result.getName().trim();
		
		System.out.println("testCaseName" +testCaseName);
		System.out.println("After method status*******************************************************>>>>>>> " +status);
		System.out.println("testMethodName" +testMethodName);
		
		try {
			mongoDBConnection.addTestCaseDataToSuiteDocument(testCaseName,testMethodName,status);
		} catch (NullPointerException npe) {
			// TODO Auto-generated catch block
			System.out.println("Mongo not connected");
		}
	}
	public void onTestSkipped(ITestResult result) {
		String testCaseName = result.getInstanceName().replace(".", "_").trim();
		String status = String.valueOf(result.getStatus());
		String testMethodName = result.getName().trim();
		
		System.out.println("testCaseName" +testCaseName);
		System.out.println("After method status*******************************************************>>>>>>> " +status);
		System.out.println("testMethodName" +testMethodName);
		
		try {
			mongoDBConnection.addTestCaseDataToSuiteDocument(testCaseName,testMethodName,status);
		} catch (NullPointerException npe) {
			// TODO Auto-generated catch block
			System.out.println("Mongo not connected");
		}
	}
	public void onFinish(ITestContext Result) 					
    {	
		String testCaseName = Result.getAllTestMethods()[0].getInstance().getClass().getName().replace(".", "_").trim();
		System.out.println(testCaseName);
		String passCount = String.valueOf(Result.getPassedTests().size());
		String failCount = String.valueOf(Result.getFailedTests().size());
		String skipCount = String.valueOf(Result.getSkippedTests().size());
		String status = "Total Pass = "+passCount+" Total Fail = "+failCount+" Total Skip = "+skipCount;
		try {
//			mongoDBConnection.addTestCaseStatusSuiteDocument(testCaseName,status);
		} catch (NullPointerException npe) {
			// TODO Auto-generated catch block
			System.out.println("Mongo not connected");
		}
    }
}
