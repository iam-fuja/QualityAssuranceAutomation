
    import com.aventstack.extentreports.ExtentTest;
    import org.openqa.selenium.*;
    import org.openqa.selenium.interactions.Actions;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.Select;
    import org.openqa.selenium.support.ui.WebDriverWait;
    import org.testng.*;
    import org.testng.annotations.*;
    import pageObject.MSDATHealthFacility;
    import pageObject.MSDATHealthOutcomesSC;
    import resources.Base;
    import resources.PageLoadUtils;

    import javax.swing.*;
    import java.io.IOException;
    import java.time.Duration;

    public class MSDATHealthFacilities extends Base {
        public WebDriver driver;
        public MSDATHealthFacility msdatHealthFacility;
        private ExtentTest test;

        @BeforeClass
        public void initializeDriver() throws IOException {
            this.driver = initializeWebDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.get(prop.getProperty("url"));

            // Use utility methods
            PageLoadUtils.waitForFullLoad(driver);  // Wait for full load
            PageLoadUtils.handleModals(driver);     // Handle modals
            msdatHealthFacility = new MSDATHealthFacility(driver);

        }


        @Test(priority = 0)
        public void messWithModal() throws InterruptedException {
            Thread.sleep(60000);
            clickWithRetry(msdatHealthFacility.getWhatsNewPopupClose());
            System.out.println("here is the first");
            Thread.sleep(5000);
            clickWithRetry(msdatHealthFacility.getTutorialSkipBtn());
            System.out.println("here is the second");
            Thread.sleep(5000);
            clickWithRetry(msdatHealthFacility.getSectionGuideClose());
            System.out.println("here is the final");
        }

        @Test(priority = 1)
        public void verifyHFacilityPage() throws InterruptedException {
            msdatHealthFacility.getDashBoardSelectnDrpDwn().click();
            msdatHealthFacility.getHealthFacility().click();
            driver.switchTo().window(driver.getWindowHandles().stream().filter(handle -> !handle.equals(driver.getWindowHandle())).findFirst().get());
            Thread.sleep(3000);
            // Loop through all open windows
//            for (String windowHandle : driver.getWindowHandles()) {
//                if (!windowHandle.equals(msdatHealthFacility.getHealthFacility())) {
//                    driver.switchTo().window(windowHandle);  // Switch to the new window
//                    break;
//                }
//            }


            String actualTitle = driver.getTitle();
            Assert.assertEquals(actualTitle, "MSDAT Nigeria | Health Facility", "Page title mismatch");
            System.out.println("here is initial");
            Thread.sleep(10000);
        }


        public void verifyHealthFacilityDashboardNav(){

        }

        @Test(priority = 2)
        public void verifyIndicatorSelect() throws InterruptedException {
            Thread.sleep(10000);
            // Click input to open dropdown
            WebElement dropdownInput = msdatHealthFacility.getHFacilityIndicatorSelector();
            System.out.println("A");
            dropdownInput.click();
            System.out.println("B");

            System.out.println("C");
            // Click the specific option (replace text with real value)
            WebElement option = msdatHealthFacility.getHFacilityIndicatorOption();
            System.out.println("D");
            option.click();
            System.out.println("E");


            //msdatHealthFacility.getHFacilityIndicatorSelector().click();
            System.out.println("This is achievable");
            Thread.sleep(5000);
            msdatHealthFacility.getHFacilityIndicatorOption().click();
            Thread.sleep(5000);

            WebElement indicatorTable = driver.findElement(By.cssSelector("div[id='the-table'] div div[class='card-header d-flex justify-content-between border-bottom-0 align-items-center base_subCard']"));

            String indicatorTableHeader = indicatorTable.getText().toString();
            System.out.println(indicatorTableHeader);

            WebElement table  = driver.findElement(By.cssSelector("div[class='w-100 d-flex justify-content-between align-items-center position-relative p-1'] b"));

            Assert.assertTrue(indicatorTable.isDisplayed(), "Table is displayed");
            Assert.assertTrue(indicatorTableHeader.contains("Proportion of Health Facilities with Basic Equipment and related indicators (with year of latest values) across Nigeria"));

        }


        @Test(priority = 3)
        public void verifyUserPrintChart(){
            clickWithRetry(msdatHealthFacility.getSubTableMenu());
            System.out.println("I have it");
            WebElement printBtn = msdatHealthFacility.getPrintBtn();
            System.out.println("I'm on print btn");
            Assert.assertTrue(printBtn.isDisplayed());
            Assert.assertTrue(printBtn.isEnabled());
            System.out.println("this finishes it");
        }

        @Test(priority = 4)
        public void verifyZonalMapExpandBtn() throws InterruptedException {
            msdatHealthFacility.expandZonalMap().click();
            System.out.println("zonal map");

//            WebElement button = msdatHealthFacility.activateColChart();
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
//            button.click();
//            Thread.sleep(3000);



            WebElement modalHeader = driver.findElement(By.id("__BVID__93___BV_modal_outer_"));
        //    System.out.println(driver.getPageSource());

            System.out.println("did we get this done?");
            // Optionally print or validate text
            String headerText = modalHeader.getText();
            //System.out.println("Modal header: " + headerText);
            Assert.assertTrue(headerText.contains("Distribution of Proportion of Health Facilities with Basic Equipment across Nigeria"));
            // Optional ExtentReports log
         //   test.pass("Modal appeared with header: " + headerText);
            System.out.println("essentially at the end");
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ESCAPE).perform();
        }




        @Test(priority = 5)
        public void verifyColumnChartExpand() throws InterruptedException {
            WebElement button = msdatHealthFacility.activateColChart();
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            button.click();
            Thread.sleep(3000);
            System.out.println("lever");

            WebElement btn = msdatHealthFacility.expandColChart();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, -200);", btn); // Scrolls up by 200 pixels
         // js.executeScript("arguments[0].scrollIntoView(true);", btn);
            btn.click();
            System.out.println("oya na");
            Thread.sleep(5000);
            System.out.println("now click");

            WebElement colModalHeader = driver.findElement(By.id("__BVID__83___BV_modal_content_") );

            System.out.println("making sense");
            System.out.println("making sense 2");
            String colHeaderText = colModalHeader.getText();
            Assert.assertTrue(colHeaderText.contains("Trend analysis of Proportion of Health Facilities with Basic Equipment across periods"));
            System.out.println(("on column chart progress"));
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ESCAPE).perform();
        }


        @Test(priority = 6)
        public void verifyUserDownloadChart() throws InterruptedException {
            WebElement tableOptn = msdatHealthFacility.getSubTableMenu();
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, -300);", tableOptn); // Scrolls up by 300 pixels
            // js.executeScript("arguments[0].scrollIntoView(true);", btn);
            tableOptn.click();
            Thread.sleep(3000);
            WebElement dwnLdBtn = msdatHealthFacility.getDwnLoadBtn();
           // dwnLdBtn.click();
            System.out.println("on the way to download");
            Assert.assertTrue(dwnLdBtn.isDisplayed());
            System.out.println("I'm on chart download btn");
            Assert.assertTrue(dwnLdBtn.isEnabled());
            System.out.println("download complete");
        }

        @Test(priority = 7)
        public void verifyZonalAnalysisTab() throws InterruptedException {
           msdatHealthFacility.getZonalAnalysisTab().click();
            System.out.println("we have it");
            Assert.assertTrue(msdatHealthFacility.getZonalAnalysisChart().isDisplayed());
            Assert.assertTrue(msdatHealthFacility.getZonalAnalysisMap().isDisplayed());
            Thread.sleep(5000);
        }

        @Test(priority = 8)
        public void verifyZonalAnalysisPrintChart() throws InterruptedException {
            Thread.sleep(5000);
            msdatHealthFacility.getZonalAnalysisChartMenu().click();
            System.out.println("Zonal Analysis sub menu");
            WebElement zonalPrintBtn = msdatHealthFacility.getZonalAnalysisPrintBtn();
            System.out.println("Zonal Analysis Print");
            Assert.assertTrue(zonalPrintBtn.isDisplayed());
            Assert.assertTrue(zonalPrintBtn.isEnabled());
            System.out.println("done done");
        }



        @Test(priority = 9)
        public void verifyZonalAnalysisChartDownload () throws InterruptedException {
            Thread.sleep(10000);

            // Click elsewhere or send ESC to close the menu
//            Actions actions = new Actions(driver);
//            actions.sendKeys(Keys.ESCAPE).perform();
//            Thread.sleep(500); // Give time for UI to update

          //  msdatHealthFacility.getZonalAnalysisChartMenu().click();
//            WebElement tableOptn = msdatHealthFacility.getSubTableMenu();
//            JavascriptExecutor js = (JavascriptExecutor) driver;
//            js.executeScript("window.scrollBy(0, -300);", tableOptn); // Scrolls up by 300 pixels
            // js.executeScript("arguments[0].scrollIntoView(true);", btn);
//            tableOptn.click();
            Thread.sleep(3000);
            WebElement downLoadBtn = msdatHealthFacility.getZonalAnalysisDownloadChart();
            // dwnLdBtn.click();
            System.out.println("zonal analysis chart download");
            Assert.assertTrue(downLoadBtn.isDisplayed());
            System.out.println("zonal analysis chart download btn");
            Assert.assertTrue(downLoadBtn.isEnabled());
            System.out.println("zonal analysis PNG download complete");
        }


        @Test(priority = 10)
        public void verifyHFacMultiSourceCompareViewIndDataYrs() throws InterruptedException {
            msdatHealthFacility.getHFacMultiSourceCompareTab().click();
            System.out.println("Multi SOucre Compare");
            Assert.assertTrue(msdatHealthFacility.getMultiSourceCompareIndicatorChart1().isDisplayed());
            Assert.assertTrue(msdatHealthFacility.getMultiSourceCompareIndicatorChart2().isDisplayed());
            Assert.assertTrue(msdatHealthFacility.getMultiSourceCompareIndicatorChart3().isDisplayed());
            Thread.sleep(5000);
        }


        @Test(priority = 11)
        public void verifyHFacIndicatorCompare(){

            msdatHealthFacility.getHFacIndicatorCompareTab().click();
            System.out.println("a new dawn Indicator COmpare");

        }

        @Test(priority = 12)
        public void verifyHFacDatasetIndicatorCompare(){

            msdatHealthFacility.getHFacDatasetCompare().click();
            System.out.println("@ dataset compare");
        }

        @AfterClass
        public void tearDown() {
//            if (driver != null) driver.quit();
        }
    }