    import com.aventstack.extentreports.ExtentReports;
    import com.aventstack.extentreports.ExtentTest;
    import com.aventstack.extentreports.Status;
    import org.openqa.selenium.WebDriver;
    import org.testng.ITestContext;
    import org.testng.ITestListener;
    import org.testng.ITestResult;
    import resources.Base;
    import resources.TestReport;

    import java.io.IOException;

    public class Listeners extends Base implements ITestListener {

        ExtentTest test;
        ExtentReports extent = TestReport.getReportObject();

        public void onTestStart(ITestResult result) {
            ITestListener.super.onTestStart(result);
            test = extent.createTest(result.getName());
        }

        public void onTestSuccess(ITestResult result) {
            ITestListener.super.onTestSuccess(result);
            test.log(Status.PASS, "Test Passed");
        }

        public void onTestFailure(ITestResult result) {
            ITestListener.super.onTestFailure(result);
            test.log(Status.FAIL, "Test Failed");
            test.fail(result.getThrowable());
            String methodName = result.getName();
            WebDriver driver = null;

            try {
                //This is used to retrieve the driver object associated with the test class
                driver = (WebDriver)result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
            } catch (Exception e) {
               // e.printStackTrace();
                throw new RuntimeException(e);
                }

            try {
                test.addScreenCaptureFromPath(takeScreenshot(methodName,driver), result.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    //    public void onTestSkipped(ITestResult result) {
    //        ITestListener.super.onTestSkipped(result);
    //    }

    //    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    //        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    //    }

    //    public void onTestFailedWithTimeout(ITestResult result) {
    //        ITestListener.super.onTestFailedWithTimeout(result);
    //    }

    //    public void onStart(ITestContext context) {
    //        ITestListener.super.onStart(context);
    //    }

        public void onFinish(ITestContext context) {
            ITestListener.super.onFinish(context);
            extent.flush();
        }
    }
