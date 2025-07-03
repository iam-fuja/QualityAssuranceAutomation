    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.interactions.Actions;
    import org.testng.Assert;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.BeforeMethod;
    import org.testng.annotations.BeforeTest;
    import org.testng.annotations.Test;
    import pageObject.MSDATHealthFinancing;
    import pageObject.Modals;
    import resources.Base;

    import java.io.IOException;


    public class MSDATHealthFinance extends Base {
        public WebDriver driver;

        public Actions actions;

        public ModalsHandling modalsHandling;
        public Modals modals;


        public MSDATHealthFinancing msdatHealthFinancing;

        @BeforeClass
        public void initializeDriver() throws IOException {
            this.driver = initializeWebDriver();
            driver.get(prop.getProperty("urlHF1"));
            driver.manage().window().maximize();

            msdatHealthFinancing = new MSDATHealthFinancing(driver);

            new ModalsHandling(driver).closeAllVisibleModals();

            System.out.println("this worked at least");

        }


        @Test
        public void verifyHFIndicator(){

          new ModalsHandling(driver).closeAllVisibleModals();
            modals = new Modals(driver);
            modals.getWhatsNewCloseBtn().click();
            System.out.println("what happened here");
         //  msdatHealthFinancing.getHFIndicatorDropdown().click();

            System.out.println("FInish");


        }








        }
