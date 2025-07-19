    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.interactions.Actions;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.Test;
    import pageObject.MSDATHealthFinance;
    import pageObject.Modals;
    import resources.Base;

    import java.io.IOException;


    public class MSDATHealthFinances extends Base {
        public WebDriver driver;

        public Actions actions;

        public ModalsHandling modalsHandling;
        public Modals modals;


        public MSDATHealthFinance msdatHealthFinance;

        @BeforeClass
        public void initializeDriver() throws IOException {
            this.driver = initializeWebDriver();
            driver.get(prop.getProperty("urlHF1"));
            driver.manage().window().maximize();

            msdatHealthFinance = new MSDATHealthFinance(driver);

            new ModalsHandling(driver).closeAllVisibleModals();

            System.out.println("this worked at least");

        }


        @Test
        public void verifyHFIndicator(){

          new ModalsHandling(driver).closeAllVisibleModals();
            modals = new Modals(driver);
            modals.getWhatsNewCloseBtn().click();
            System.out.println("what happened here");
         //  msdatHealthFinance.getHFIndicatorDropdown().click();

            System.out.println("FInish");


        }








        }
