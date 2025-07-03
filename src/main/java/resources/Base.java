    package resources;

    import org.apache.commons.io.FileUtils;
    import org.openqa.selenium.OutputType;
    import org.openqa.selenium.TakesScreenshot;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.chrome.ChromeDriver;
    import org.openqa.selenium.chrome.ChromeOptions;

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.io.IOException;
    import java.time.Duration;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Properties;

    public class Base {
        public FileInputStream fis;
        public Properties prop;
        public WebDriver driver;
        public ChromeOptions options;




        public WebDriver initializeWebDriver() throws IOException {
            fis = new  FileInputStream(System.getProperty("user.dir")+"/src/main/java/resources/configuration.properties");
            prop = new Properties();
            prop.load(fis);

            String browserName = prop.getProperty("browser");
            if (browserName.equalsIgnoreCase("Chrome")){
                System.setProperty("webdriver,chrome.driver",System.getProperty("user.dir")+"\\src\\main\\java\\resources\\chromedriver");
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

                // OPTIONAL: Run headless in CI
                if (System.getenv("CI") != null) {
                    options.addArguments("--headless=new");
                }

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
            return driver;
        }


        public String takeScreenshot(String testcaseName, WebDriver driver) throws IOException {
            File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String dest = System.getProperty("user.dir")+"/reports/"+testcaseName+".jpg";
            FileUtils.copyFile(src, new File(dest));
            return dest;
        }

    }
