    package resources;


    import io.github.bonigarcia.wdm.WebDriverManager;
    import org.apache.commons.io.FileUtils;
    import org.openqa.selenium.*;
    import org.openqa.selenium.chrome.ChromeDriver;
    import org.openqa.selenium.chrome.ChromeOptions;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;

    import java.io.*;
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
        protected WebDriverWait wait;
        public ChromeOptions options;


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
                options = new ChromeOptions();


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
                    options.addArguments("--disable-infobars");
                    options.addArguments("--disable-blink-features=AutomationControlled");
                    // Important for headless mode
                    options.addArguments("--remote-allow-origins=*");
                    options.setAcceptInsecureCerts(true);
                }

                 this.driver = new ChromeDriver(options);
                 wait = new WebDriverWait(driver, Duration.ofSeconds(480));

                //configure driver to manage flow with implicit wait
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(480));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(480));
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




        public String takeScreenshot(String testcaseName, WebDriver driver) throws IOException {
            // Ensure the directory exists
            new File("target/reports").mkdirs();
            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
       //     String dest = System.getProperty("user.dir")+"/reports/"+testcaseName+".jpg";
            String dest = "target/reports/" + testcaseName + ".jpg";
            FileUtils.copyFile(src, new File(dest));
            return dest;
        }


        //FOR CI PURPOSES
        protected void clickWithRetry(WebElement element) {
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
        public String takerScreenshot(String testcaseName, WebDriver driver) throws IOException {
            // Create reports directory if it doesn't exist
          //  new File(System.getProperty("user.dir")+"/reports/").mkdirs();

            // Ensure the directory exists
            new File("target/reports").mkdirs();

            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
         //   String dest = System.getProperty("user.dir")+"/reports/"+testcaseName+"_"+timestamp+".png";
            String dest = "target/reports/" + testcaseName + ".jpg";
            FileUtils.copyFile(src, new File(dest));
            return dest;
        }

    }
