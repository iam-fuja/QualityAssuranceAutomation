    package resources;



    import io.github.bonigarcia.wdm.WebDriverManager;
    import org.apache.commons.io.FileUtils;
    import org.openqa.selenium.*;
    import org.openqa.selenium.chrome.ChromeDriver;
    import org.openqa.selenium.chrome.ChromeOptions;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.io.IOException;
    import java.text.SimpleDateFormat;
    import java.time.Duration;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Properties;

    public class Base {
        public FileInputStream fis;
        public Properties prop;
        public WebDriver driver;
        public ChromeOptions options;
        public WebDriverManager webDriverManager;


        public WebDriver initializeWebDriver() throws IOException {
            fis = new  FileInputStream(System.getProperty("user.dir")+"/src/main/java/resources/configuration.properties");
            prop = new Properties();
            prop.load(fis);

            String browserName = prop.getProperty("browser");
            if (browserName.equalsIgnoreCase("Chrome")){
                WebDriverManager.chromedriver().setup();
               // System.setProperty("webdriver,chrome.driver",System.getProperty("user.dir")+"\\src\\main\\java\\resources\\chromedriver");
                options = new ChromeOptions();

                //configure driver to run browser in incognito mode and attempt to disable geo-location verification
                // Removed temporarily: "--incognito"
                options.addArguments( "--incognito", "--disable-geolocation" , "--disable modal", "\"--no-sandbox\"", "--disable-dev-shm-usage", "--disable-notifications");


                //configure driver to manage windows alerts notifications and geo-location verification requests
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
                prefs.put("profile.default_content_setting_values.geolocation", 2);
                prefs.put("profile.default_content_setting_values.popup", 2);
                prefs.put("profile.default_content_setting_values.modal", 2);
                options.setExperimentalOption("prefs",prefs);

                ///******///////

                // OPTIONAL: Run headless in CI
//                if (System.getenv("CI") != null) {
//                    options.addArguments("--headless=new");
//                    //explicit wait for CI runs
//                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(240));
//                }

                ///******///////

                // In your Base class constructor/setup:
               // ChromeOptions options = new ChromeOptions();

                // Unified CI detection and configuration
                boolean isCI = System.getenv("CI") != null || System.getProperty("CI") != null || System.getenv("CI") != null;

                if (isCI) {
                    // Headless configuration
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--no-sandbox");

                    // Timeout configuration
                    this.driver = new ChromeDriver(options);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(35));
                    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
                }
                    ///******///////

                this.driver = new ChromeDriver(options);

                //configure driver to manage flow with implicit wait
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
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
            ((JavascriptExecutor)driver).executeScript("window.focus();");

            // Before each click
            try {
                driver.findElement(By.cssSelector("body")).click(); // Reset focus
            } catch (Exception e) {}


            return driver;
        }




        public String takeScreenshot(String testcaseName, WebDriver driver) throws IOException {
            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String dest = System.getProperty("user.dir")+"/reports/"+testcaseName+".jpg";
            FileUtils.copyFile(src, new File(dest));
            return dest;
        }


        //FOR CI PURPOSES
        protected void clickWithRetry(WebElement element) {
            int attempts = 0;
            while (attempts < 3) {
                try {
                    new WebDriverWait(driver, Duration.ofSeconds(15))
                            .until(ExpectedConditions.elementToBeClickable(element));
                    element.click();
                    return;
                } catch (Exception e) {
                    attempts++;
                    ((JavascriptExecutor)driver).executeScript(
                            "arguments[0].scrollIntoView({block: 'center'});", element);
                    try { Thread.sleep(500); } catch (InterruptedException ie) {
                        System.out.println("Retry isn't happening");
                    }
                }
            }
            throw new RuntimeException("Failed to click after 3 attempts");
        }


        //FOR CI PURPOSES
        public String takerScreenshot(String testcaseName, WebDriver driver) throws IOException {
            // Create reports directory if it doesn't exist
            new File(System.getProperty("user.dir")+"/reports/").mkdirs();

            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String dest = System.getProperty("user.dir")+"/reports/"+testcaseName+"_"+timestamp+".png";
            FileUtils.copyFile(src, new File(dest));
            return dest;
        }




    }
