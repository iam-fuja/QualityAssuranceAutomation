    package resources;


    import io.github.bonigarcia.wdm.WebDriverManager;
    import org.apache.commons.io.FileUtils;
    import org.openqa.selenium.*;
    import org.openqa.selenium.chrome.ChromeDriver;
    import org.openqa.selenium.chrome.ChromeOptions;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;

    import java.io.*;
    import java.nio.charset.StandardCharsets;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.text.SimpleDateFormat;
    import java.time.Duration;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Properties;
    import java.util.zip.ZipEntry;
    import java.util.zip.ZipOutputStream;

    public class Base {
        public FileInputStream fis;
        public Properties prop;
        public WebDriver driver;
        protected WebDriverWait wait;
        public ChromeOptions options;


        // ========== INSERTION POINT         // ========== INSERTION POINT 6: 6: JVM Shutdown Hook ==========
        // ADD THIS STATIC BLOCK right here (after your class variables):
        static {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("JVM shutdown detected, cleaning up resources...");
            }));
        }


        // ========== INSERTION POINT 1: Enhanced Chrome Options Method ==========
        // ADD THIS NEW METHOD after your class variables:
        private ChromeOptions getEnhancedChromeOptions() {
            ChromeOptions options = new ChromeOptions();

            // Your existing basic configurations
            options.addArguments("--incognito", "--disable-geolocation", "--disable modal",
                    "--no-sandbox", "--disable-dev-shm-usage", "--disable-notifications");

            // Enhanced stability options for stale driver prevention
            options.addArguments("--remote-debugging-port=9222");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-web-security");
            options.addArguments("--disable-features=VizDisplayCompositor");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-background-timer-throttling");
            options.addArguments("--disable-backgrounding-occluded-windows");
            options.addArguments("--disable-renderer-backgrounding");

            // CI Environment detection and additional options
            boolean isCI = isRunningInCI();
            boolean isHeadless = Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "false"));

            if (isCI || isHeadless) {
                System.out.println("Running in CI/Headless mode with enhanced stability options");
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--disable-gpu");
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--disable-info bars");
                options.setAcceptInsecureCerts(true);
            }

            // Your existing prefs configuration
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("profile.default_content_setting_values.notifications", 2);
            prefs.put("profile.default_content_setting_values.geolocation", 2);
            prefs.put("profile.default_content_setting_values.popup", 2);
            prefs.put("profile.default_content_setting_values.modal", 2);
            options.setExperimentalOption("prefs", prefs);

            return options;
        }

        // CI Detection Method
        private boolean isRunningInCI() {
            return System.getenv("CI") != null ||
                    System.getenv("JENKINS_URL") != null ||
                    System.getenv("GITHUB_ACTIONS") != null ||
                    "true".equalsIgnoreCase(System.getenv("CI"));
        }




        public WebDriver initializeWebDriver() throws IOException {
            //local machine pathway
//            fis = new  FileInputStream(System.getProperty("user.dir")+"/src/main/java/resources/configuration.properties");
//            prop = new Properties();
//            prop.load(fis);

            //compatible for both local and CI env
            prop = new Properties();
            // Load defaults from classpath (inside JAR)
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("configuration.properties")) {
                if (inputStream != null) {
                    prop.load(inputStream);
                } else {
                    throw new FileNotFoundException("configuration.properties not found in classpath.");
                }
            }

            // String browserName = prop.getProperty("browser");

            // Try to fetch from environment variable (e.g., CI), fallback to properties file
            String browserName = System.getenv().getOrDefault("BROWSER", prop.getProperty("browser"));
            boolean isHeadless = Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "false"));
            boolean isCI = "true".equalsIgnoreCase(System.getenv("CI"));

            if (browserName.equalsIgnoreCase("Chrome")){
                 WebDriverManager.chromedriver().setup();
               // System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\src\\main\\java\\resources\\chromedriver");
          /*      options = new ChromeOptions();


                //configure driver to run browser in incognito mode and attempt to disable geo-location verification
                options.addArguments( "--incognito", "--disable-geolocation" , "--disable modal", "--no-sandbox", "--disable-dev-shm-usage", "--disable-notifications");


                //configure driver to manage windows alerts notifications and geo-location verification requests
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
                prefs.put("profile.default_content_setting_values.geolocation", 2);
                prefs.put("profile.default_content_setting_values.popup", 2);
                prefs.put("profile.default_content_setting_values.modal", 2);
                options.setExperimentalOption("prefs",prefs);


                if (isCI || isHeadless) {
                    // Headless configuration
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1008");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage"); // Avoid /dev/shm issues
                    options.addArguments("--disable-info bars");
                    options.addArguments("--disable-blink-features=AutomationControlled");
                    // Important for headless mode
                    options.addArguments("--remote-allow-origins=*");
                    options.setAcceptInsecureCerts(true);
                }
*/



                // REPLACE your existing options configuration with this line:
                options = getEnhancedChromeOptions(); // USE THE NEW METHOD


                 this.driver = new ChromeDriver(options);
                 wait = new WebDriverWait(driver, Duration.ofSeconds(120));

                //configure driver to manage flow with implicit wait
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(480));
                driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
            }
            else if (browserName.equalsIgnoreCase("Internet Explorer")){
                //code to initialize Internet Explorer driver

            }
            else if (browserName.equalsIgnoreCase("Firefox")){
                //code to initialize Firefox driver

            }

            //INTRODUCED to sanitize CI pipeline
            // Right after driver initialization
            driver.manage().deleteAllCookies();
            try {
                ((JavascriptExecutor)driver).executeScript("window.focus();");
            } catch (Exception e) {
                System.out.println("Skipping focus adjustments in CI");
            }

            // Before each click
            try {
                driver.findElement(By.cssSelector("body")).click(); // Reset focus
            } catch (Exception e) {System.out.println("Skipping focus adjustments in CI 2");}

            return driver;
        }



        // ========== INSERTION POINT 5: Driver Health Check Methods ==========
        // ADD THESE NEW METHODS after your initializeWebDriver method:
        protected boolean isDriverAlive() {
            if (driver == null) {
                return false;
            }

            try {
                driver.getCurrentUrl();
                return true;
            } catch (Exception e) {
                System.err.println("Driver health check failed: " + e.getMessage());
                return false;
            }
        }

        protected void ensureDriverIsAlive() {
            if (!isDriverAlive()) {
                System.out.println("Driver is stale, recreating...");
                cleanupDriver();
                try {
                    initializeWebDriver();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to recreate driver", e);
                }
            }
        }

        // ========== INSERTION POINT 4: Cleanup Method ==========
        // ADD THIS NEW METHOD:
        public void cleanupDriver() {
            System.out.println("Cleaning up WebDriver...");

            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    System.err.println("Error during driver cleanup: " + e.getMessage());
                } finally {
                    driver = null;
                    wait = null;
                }
            }

            System.out.println("WebDriver cleanup complete");
        }





        public String takeScreenshot(String testcaseName, WebDriver driver) throws IOException {
            // Ensure the directory exists
            new File("target/reports").mkdirs();
            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
       //   String dest = System.getProperty("user.dir")+"/reports/"+testcaseName+".jpg";
       //   String dest = "target/reports/screenshots/" + testcaseName + ".jpg";
            String dest = "target/reports/" + testcaseName + ".jpg";
            FileUtils.copyFile(src, new File(dest));
            return dest;
        }

        //FOR CI PURPOSES
        protected void clickWithRetry(WebElement element) {

            // Add health check before retry
            ensureDriverIsAlive();

            int attempts = 0;
            while (attempts < 3) {
                try {
                    new WebDriverWait(driver, Duration.ofSeconds(240))
                            .until(ExpectedConditions.elementToBeClickable(element));
                    element.click();
                    return;
                } catch (Exception e) {
                    attempts++;
                    ((JavascriptExecutor)driver).executeScript(
                            "arguments[0].scrollIntoView({block: 'center'});", element);
                    try { Thread.sleep(5000); } catch (InterruptedException ie) {
                        System.out.println("Retry isn't happening");
                    }
                }
            }
            throw new RuntimeException("Failed to click after 3 attempts");
        }


        //FOR CI PURPOSES
//        public String takerScreenshot(String testcaseName, WebDriver driver) throws IOException {
//            // Create reports directory if it doesn't exist
//          //  new File(System.getProperty("user.dir")+"/reports/").mkdirs();
//
//            // Ensure the directory exists
//            new File("target/reports").mkdirs();
//
//            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//         // String dest = System.getProperty("user.dir")+"/reports/"+testcaseName+"_"+timestamp+".png";
//         // String dest = "target/reports/" + testcaseName + ".jpg";
//            String dest = "target/reports/screenshots/" + testcaseName + ".jpg";
//
//            FileUtils.copyFile(src, new File(dest));
//            return dest;
//        }



        private static boolean isFirstRun = true;
        private static final String REPORT_DIR = "target/reports";

        public String takerScreenshot(String methodName, WebDriver driver) {
            String dest = null;
            try {
                if (isFirstRun) {
                    FileUtils.deleteQuietly(new File(REPORT_DIR + "/TestReportBundle.zip"));
                    FileUtils.deleteQuietly(new File(REPORT_DIR + "/summary.txt"));
                    isFirstRun = false;
                }

                // Organize per test class if available via stack trace
                String callingClass = getCallingTestClass();
                String classFolder = REPORT_DIR + "/screenshots/" + callingClass;
                new File(classFolder).mkdirs();

                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                dest = classFolder + "/" + methodName + "_" + timestamp + ".jpg";
                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(src, new File(dest));

                // Log to summary
                String log = String.format("[%s] %s - Screenshot: %s%n",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                        methodName,
                        dest);
                FileUtils.writeStringToFile(new File(REPORT_DIR + "/summary.txt"), log, StandardCharsets.UTF_8, true);

                // ZIP once
                File zipFile = new File(REPORT_DIR + "/TestReportBundle.zip");
                if (!zipFile.exists()) {
                    ReportDeliveryUtil.zipFolder(REPORT_DIR, zipFile.getAbsolutePath());
                    if (enableEmail) ReportDeliveryUtil.emailZipReport(zipFile);
                    if (enableDriveUpload) ReportDeliveryUtil.uploadToDrive(zipFile);
                }

            } catch (Exception e) {
                System.err.println("❌ Screenshot error: " + e.getMessage());
            }
            return dest;
        }

        private String getCallingTestClass() {
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                if (element.getClassName().contains("Test")) {
                    return element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1);
                }
            }
            return "UnknownTest";
        }

        // These can be toggled if needed
        private final boolean enableEmail = true;
        private final boolean enableDriveUpload = true;



    }
