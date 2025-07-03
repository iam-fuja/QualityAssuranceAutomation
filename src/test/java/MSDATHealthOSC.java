    import org.openqa.selenium.WebDriver;
    import org.testng.Assert;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.Test;
    import pageObject.MSDATHealthOutcomesSC;
    import resources.Base;

    import java.io.IOException;

    public class MSDATHealthOSC extends Base {
        public WebDriver driver;
        public MSDATHealthOutcomesSC msdatHealthOutcomesSC;

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
