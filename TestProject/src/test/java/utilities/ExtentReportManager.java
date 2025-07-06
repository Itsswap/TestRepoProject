package utilities;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import base.CommonMethods;

public class ExtentReportManager extends CommonMethods implements ITestListener {

	public ExtentReportManager() throws Exception {
		super();
	}

	public ExtentSparkReporter extentreport;
	public ExtentReports report;
	public ExtentTest test;
	public ThreadLocal<ExtentTest> extentTestThread = new ThreadLocal<>();

	public void onStart(ITestContext context) {
		extentreport = new ExtentSparkReporter(System.getProperty("user.dir") + "./reports/ExtentReport.html");
		report = new ExtentReports();
		report.attachReporter(extentreport);
		report.setSystemInfo("Project", "Test Project");
		report.setSystemInfo("Tester", "Swapnil Gaikwad");
	}

	@Override
	public void onTestStart(ITestResult result) {
		test = report.createTest(result.getMethod().getMethodName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		test.log(Status.PASS, "Test passed: " + result.getMethod().getMethodName());
	}

	@Override
	public void onTestFailure(ITestResult result) {
		test.log(Status.FAIL, "Test failed: " + result.getMethod().getMethodName());
		test.log(Status.FAIL, result.getThrowable());

		// Optional: Capture and attach screenshot if WebDriver is accessible
		Object testClass = result.getInstance();
		WebDriver driver = getDriverFromTestInstance(testClass);

		if (driver != null) {
			String screenshotPath = captureScreenshot(driver, result.getMethod().getMethodName());
			test.addScreenCaptureFromPath(screenshotPath);
		} else {
			test.warning("WebDriver instance is null. Screenshot not captured.");
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		test.log(Status.SKIP, "Test skipped: " + result.getMethod().getMethodName());
	}

	@Override
	public void onFinish(ITestContext context) {
		report.flush();
	}

}
