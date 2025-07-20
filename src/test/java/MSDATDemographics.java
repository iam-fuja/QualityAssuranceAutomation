    import org.openqa.selenium.WebDriver;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.Test;
    import pageObject.MSDATDemographic;
    import resources.Base;

    import java.io.IOException;

    public class MSDATDemographics extends Base {
        public WebDriver driver;
        public MSDATDemographic msdatDemographic;
      //  public MSDATHealthFacilities msdatHealthFacilities;


        @Test
        public void initializeDriver() throws IOException {
            this.driver = initializeWebDriver();
            driver.manage().window().maximize();

            driver.get(prop.getProperty("url"));
            System.out.println("Initialize driver for demographic");
        }


        }
