    import org.openqa.selenium.By;
    import org.openqa.selenium.JavascriptExecutor;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.interactions.Actions;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;
    import org.testng.Assert;
    import org.testng.ITestContext;
    import org.testng.annotations.*;
    import pageObject.MSDATHealthDemographic;
    import pageObject.MSDATHealthFacility;
    import resources.Base;
    import resources.TestReport;

    import java.io.IOException;
    import java.time.Duration;

    public class MSDATHealthDemographics extends Base {
        public WebDriver driver;
        public MSDATHealthDemographic msdatDemographic;
        public MSDATHealthFacilities msdatHealthFacilities;



        @BeforeClass
        public void initializeDriver() throws IOException, InterruptedException {
            this.driver = initializeWebDriver();
            driver.manage().window().maximize();
            driver.get(prop.getProperty("url"));
            System.out.println("Initialize driver for demographic");
           msdatDemographic = new MSDATHealthDemographic(driver);
            Thread.sleep(11000);
        }



       @Test(priority = 0)
        public void handleModalMSDATDemographics() throws InterruptedException {
           Thread.sleep(25000);
            msdatHealthFacilities = new MSDATHealthFacilities(new MSDATHealthFacility(driver));
            msdatHealthFacilities.messWithModal();
            System.out.println("Demographics modals handled successfully");
        }



        @Test(priority = 1)
        public void verifyMSDATDemographicPage() throws InterruptedException {
            msdatHealthFacilities = new MSDATHealthFacilities(new MSDATHealthFacility(driver));
            msdatHealthFacilities.msdatHealthFacility.getDashBoardSelectnDrpDwn().click();
            System.out.println("is drop down happening");
            msdatDemographic.getMSDATDemographicsPage().click();

            String originalWindow = driver.getWindowHandle();
//            new WebDriverWait(driver, Duration.ofSeconds(40))
//                    .until(driver -> driver.getWindowHandles().size() > 1);
            driver.getWindowHandles();
            new WebDriverWait(driver, Duration.ofSeconds(40))
                    .until(driver -> driver.getWindowHandles().size() > 1);
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
//            Thread.sleep(5000);
           // String word = driver.findElement(By.xpath("//h2[@class='main-text.d-inline-block']")).getText();
            Thread.sleep(10000
            );
            String word = driver.findElement(By.cssSelector(".main-text.d-inline-block")).getText();

            System.out.println(word);
            System.out.println(driver.getTitle());
            Assert.assertTrue(word.contains("Demographics"));
            System.out.println("is this possible");
        }



        @Test(priority = 2)
        public void verifyMsdatDemographicIndicatorTabSelect() throws InterruptedException {
            Thread.sleep(10000);
            msdatDemographic.getDemographicsIndicatorSelector().click();
            msdatDemographic.getDemographicIndicatorOption().click();
            msdatDemographic.getDemographicIndicatorOption().click();
            WebElement indicatorTable = msdatDemographic.getDemographicsTableHeader();
            String demographicIndicatorTableHeader = indicatorTable.getText();
            System.out.println(demographicIndicatorTableHeader);
            Assert.assertTrue(indicatorTable.isDisplayed(), "Table is displayed");
            Assert.assertTrue(demographicIndicatorTableHeader.contains("Total population and related indicators (with year of latest values) across Nigeria."));
        }



        @Test(priority = 3)
        public void verifyMsdatDemographicLocationChangeOutput() throws InterruptedException {
            Thread.sleep(10000);
            msdatDemographic.getDemographicLocationDropdown().click();
            msdatDemographic.getDemographicLocationDropdownOption().click();
            System.out.println(driver.findElement(By.cssSelector(".w-100.d-flex.justify-content-between.align-items-center.position-relative.p-1")).getText());
            Assert.assertTrue(driver.findElement(By.cssSelector(".w-100.d-flex.justify-content-between.align-items-center.position-relative.p-1")).getText().contains("Abia"));
        }



        @Test(priority = 4)
        public void verifyMsdatDemographicMoreInfoIcon() throws InterruptedException {
            Actions action = new Actions(this.driver);
            Thread.sleep(10000);
//            JavascriptExecutor js = (JavascriptExecutor) driver;
//            js.executeScript("arguments[0].scrollIntoView(true);", msdatDemographic.getDemographicInfoIcon());
//            js.executeScript("arguments[0].click();", msdatDemographic.getDemographicInfoIcon());
            action.doubleClick(msdatDemographic.getDemographicInfoIcon());
            Assert.assertTrue(msdatDemographic.getDemographicInfoIcon().isEnabled());
//            Assert.assertTrue(driver.findElement(By.cssSelector(".modal-title")).getText().contains("NPC"));
//            msdatDemographic.getCloseInfoModalPage().click();
        }

        @Test(priority = 5)
        public void verifyDemoIndicatorOverviewPrintChart (){
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", msdatDemographic.getDemoIndSubMenu());
            js.executeScript("window.scrollBy(0, -100);", msdatDemographic.getDemoIndSubMenu());
            //js.executeScript("arguments[0].click();", msdatDemographic.getDemoIndSubMenu());


            System.out.println("Tag name: " + msdatDemographic.getDemoIndSubMenu().getTagName());
            System.out.println("Is displayed: " + msdatDemographic.getDemoIndSubMenu().isDisplayed());
            System.out.println("OuterHTML: " + ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].outerHTML;", msdatDemographic.getDemoIndSubMenu()));


            new WebDriverWait(driver, Duration.ofSeconds(40))
                    .until(ExpectedConditions.visibilityOf(msdatDemographic.getDemoIndSubMenu()));
            msdatDemographic.getDemoIndSubMenu().click();

            Assert.assertTrue(msdatDemographic.getDemoIndSubMenuPrintBtn().isDisplayed());
            Assert.assertTrue(msdatDemographic.getDemoIndSubMenuPrintBtn().isEnabled());
        }




        @AfterClass
        public void tearDown(ITestContext context) {

            //if (driver != null) driver.quit();
            TestReport.exportReportSummary(context);
        }


        }
