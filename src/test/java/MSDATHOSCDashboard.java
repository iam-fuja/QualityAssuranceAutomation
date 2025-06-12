    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.chrome.ChromeDriver;
    import org.openqa.selenium.chrome.ChromeOptions;
    import org.testng.Assert;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.Test;
    import pageObject.MSDATHealthOutcomesServiceCoverageDashboard;
    import resources.Base;

    import java.io.IOException;
    import java.time.Duration;
    import java.util.HashMap;
    import java.util.Map;

    public class MSDATHOSCDashboard extends Base {
        public WebDriver driver;
        public MSDATHealthOutcomesServiceCoverageDashboard msdatHealthOutcomesServiceCoverageDashboard;

        @BeforeClass
        public void initializeDriver() throws IOException, InterruptedException {
            this.driver = initializeWebDriver();

            driver.manage().window().maximize();
            driver.get(prop.getProperty("urlHOSC"));
        }



        @Test
        public void verifyHOSCDPage(){
            String actualTitle = driver.getTitle();
            String expectedTitle = "MSDATy Nigeria";
            Assert.assertEquals(actualTitle,expectedTitle, "Page title is incorrect");
        }

        }
