    import org.openqa.selenium.WebDriver;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.BeforeTest;
    import org.testng.annotations.Test;
    import pageObject.MSDATDemographic;
    import pageObject.MSDATHealthFacility;
    import resources.Base;

    import java.io.IOException;

    public class MSDATDemographics extends Base {
        public WebDriver driver;
        public MSDATDemographic msdatDemographic;
        public MSDATHealthFacilities msdatHealthFacilities;



        @BeforeTest
        public void initializeDriver() throws IOException {
            this.driver = initializeWebDriver();
            driver.manage().window().maximize();
            driver.get(prop.getProperty("url"));
            System.out.println("Initialize driver for demographic");
          //  msdatDemographic = new MSDATDemographic(driver);
        }

       @Test(priority = 0)
        public void handleModal() throws InterruptedException {
            msdatHealthFacilities = new MSDATHealthFacilities(new MSDATHealthFacility(driver));
            msdatHealthFacilities.messWithModal();
            System.out.println("modals handled successfully");
        }


        public void verifyMSDATDemographic(){

        }

        }
