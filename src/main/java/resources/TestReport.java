package resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class TestReport {

    public static ExtentReports getReportObject(){
        String path = System.getProperty("user.dir")+"/reports/index.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName("MSDAT Daily Automation");
        reporter.config().setDocumentTitle("Daily Test Results");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Quality Assurance Tester", "Makanjuola Oyekola");
        return extent;

    }
}
