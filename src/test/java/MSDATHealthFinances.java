    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.interactions.Actions;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;
    import org.testng.Assert;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.Test;
    import pageObject.MSDATHealthFinance;
    import pageObject.Modals;
    import resources.Base;
    import resources.PageLoadUtils;

    import java.io.IOException;
    import java.time.Duration;


    public class MSDATHealthFinances extends Base {
        public WebDriver driver;
        public WebDriverWait wait;
        public MSDATHealthFinance msdatHealthFinance;
        public MSDATHealthFacilities msdatHealthFacilities;
        public Actions actions;
        public Modals modals;



        @BeforeClass
        public void initializeDriver() throws IOException, InterruptedException {
            this.driver = initializeWebDriver();
         //   wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            driver.manage().window().maximize();
        //  driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
            driver.get(prop.getProperty("url"));
            System.out.println("Initialize driver for finance");
            msdatHealthFinance = new MSDATHealthFinance(driver);
            System.out.println("Finances driver initiated");

            // Use utility methods
            PageLoadUtils.waitForFullLoad(driver);  // Wait for full load
            System.out.println("PageLoadUtils.waitForFullLoad");
            PageLoadUtils.handleModals(driver);     // Handle modals
            System.out.println("PageLoadUtils.handleModals");
            Thread.sleep(11000);
        }



        @Test(priority = 0)
        public void handleModalMSDATFinances() throws InterruptedException {
            Thread.sleep(25000);
            new WebDriverWait(driver, Duration.ofSeconds(240))
                        .until(ExpectedConditions.visibilityOf(msdatHealthFinance.getWhatsNewPopupClose()));
                clickWithRetry(msdatHealthFinance.getWhatsNewPopupClose());
                System.out.println("here is the first");

//            new WebDriverWait(driver, Duration.ofSeconds(240))
//                    .until(ExpectedConditions.visibilityOf(msdatHealthFinance.getTutorialSkipBtn()));
//            clickWithRetry(msdatHealthFinance.getTutorialSkipBtn());
//            System.out.println("here is the second");
//
//            new WebDriverWait(driver, Duration.ofSeconds(240))
//                    .until(ExpectedConditions.visibilityOf(msdatHealthFinance.getSectionGuideClose()));
//            clickWithRetry(msdatHealthFinance.getSectionGuideClose());
//            System.out.println("here is the final");

            System.out.println("Finance modals handled successfully");
            }




        @Test(priority = 1)
        public void verifyMSDATFinancesPage() throws InterruptedException {
            msdatHealthFinance.getDashBoardSelectnDrpDwn().click();
            System.out.println("drop down happening for Finance");
            msdatHealthFinance.getMSDATFinancePage().click();

//            new WebDriverWait(driver, Duration.ofSeconds(240))
//                    .until(ExpectedConditions.elementToBeClickable(msdatHealthFinance.getDashBoardSelectnDrpDwn()));
//            msdatHealthFinance.getDashBoardSelectnDrpDwn().click();
//
//            new WebDriverWait(driver, Duration.ofSeconds(240))
//                    .until(ExpectedConditions.elementToBeClickable(msdatHealthFinance.getMSDATFinancePage()));
//            msdatHealthFinance.getMSDATFinancePage().click();

            String originalWindow = driver.getWindowHandle();
            driver.getWindowHandles();
            new WebDriverWait(driver, Duration.ofSeconds(40))
                    .until(driver -> driver.getWindowHandles().size() > 1);
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            // String word = driver.findElement(By.xpath("//h2[@class='main-text.d-inline-block']")).getText();
            String word = driver.findElement(By.cssSelector(".main-text.d-inline-block")).getText();
            System.out.println(word);
            System.out.println(driver.getTitle());
            Assert.assertTrue(word.contains("Financing"));
            System.out.println("Finance this possible");
        }





        }
