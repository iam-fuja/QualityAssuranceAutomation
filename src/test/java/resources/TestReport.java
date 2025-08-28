    package resources;

    import com.aventstack.extentreports.ExtentReports;
    import com.aventstack.extentreports.reporter.ExtentSparkReporter;
    import org.testng.ITestContext;

    import java.io.*;
    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.zip.ZipEntry;
    import java.util.zip.ZipOutputStream;

    public class TestReport {

//        public static ExtentReports getReportObject(){
//          //String path = System.getProperty("user.dir")+"/reports/index.html";
//            String path = "target/reports/" + "/index.html";
//            ExtentSparkReporter reporter = new ExtentSparkReporter(path);
//            reporter.config().setReportName("MSDAT Daily Automation");
//            reporter.config().setDocumentTitle("Daily Test Results");
//
//            ExtentReports extent = new ExtentReports();
//            extent.attachReporter(reporter);
//            extent.setSystemInfo("Quality Assurance Tester", "Makanjuola Oyekola");
//            return extent;
//
//        }
        private static final String REPORT_DIR = "target/reports/";
        private static final String SCREENSHOT_DIR = REPORT_DIR + "screenshots/";
        private static final String SUMMARY_FILE = REPORT_DIR + "summary.txt";
        public static final String ZIP_FILE = REPORT_DIR + "report_bundle.zip";

        //modification-1
        private static ExtentReports extentInstance;

        public static ExtentReports getReportObject() {
            new File(SCREENSHOT_DIR).mkdirs(); // Ensure screenshot folder exists
            String path = REPORT_DIR + "index.html";


            ExtentSparkReporter reporter = new ExtentSparkReporter(path);
            reporter.config().setReportName("MSDAT Daily Automation");
            reporter.config().setDocumentTitle("Daily Test Results");
//            ExtentReports extent = new ExtentReports();
//            extent.attachReporter(reporter);
//            extent.setSystemInfo("Quality Assurance Tester", "Makanjuola Oyekola");
//            return extent;
            extentInstance = new ExtentReports();
            extentInstance.attachReporter(reporter);
            extentInstance.setSystemInfo("Quality Assurance Tester", "Makanjuola Oyekola");
            return extentInstance;
        }


        // ADD THIS METHOD HERE:
        public static void flushReport() {
            if (extentInstance != null) {
                extentInstance.flush();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }



        /**
         * Call this in @AfterSuite and pass in the TestNG context
         */
        public static void exportReportSummary(ITestContext context) {
            // Ensure report is flushed first
            flushReport();
            try {
                int passed = context.getPassedTests().size();
                int failed = context.getFailedTests().size();
                int skipped = context.getSkippedTests().size();
                String status = failed > 0 ? "FAILED" : "PASSED";

                String runDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String reportPath = REPORT_DIR + "index.html";

                File reportFile = new File(reportPath);

                // Basic existence check (should rarely fail now that we flush first)
                if (!reportFile.exists()) {
                    System.err.println("❌ Report file not found: " + reportPath);
                    return;
                }

                // 1. Write summary.txt
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(SUMMARY_FILE))) {
                    writer.write("status=" + status + "\n");
                    writer.write("date=" + runDate + "\n");
                    writer.write("report=" + reportPath + "\n");
                    writer.write("passed=" + passed + "\n");
                    writer.write("failed=" + failed + "\n");
                    writer.write("skipped=" + skipped + "\n");
                }

                // 2. Create zip with report + screenshots
                try (FileOutputStream fos = new FileOutputStream(ZIP_FILE);
                     ZipOutputStream zipOut = new ZipOutputStream(fos)) {

//                    addToZip(new File(reportPath), zipOut); // Add index.html

                    addToZip(reportFile, zipOut); // Add index.html

                    // Add screenshots in SCREENSHOT_DIR
                    File[] screenshots = new File(SCREENSHOT_DIR).listFiles((dir, name) -> name.endsWith(".jpg"));
                    if (screenshots != null) {
                        for (File screenshot : screenshots) {
                            addToZip(screenshot, zipOut);
                        }
                    }
                }

                System.out.println("✅ Report summary exported with status: " + status);
                ReportDeliveryUtil.emailTestReport(new File(ZIP_FILE), context);

            } catch (IOException e) {
                System.err.println("❌ Failed to export report summary: " + e.getMessage());
            }
        }

        private static void addToZip(File file, ZipOutputStream zipOut) throws IOException {
            try (FileInputStream fis = new FileInputStream(file)) {
//                String entryName = file.getAbsolutePath().replace(new File(REPORT_DIR).getAbsolutePath() + File.separator, "");
                String entryName = file.getName(); // Just use the filename for cleaner zip structure
                zipOut.putNextEntry(new ZipEntry(entryName));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) >= 0) {
                    zipOut.write(buffer, 0, len);
                }
                zipOut.closeEntry();
            }
        }


    }
