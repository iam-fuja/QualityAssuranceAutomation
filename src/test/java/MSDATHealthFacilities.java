
    import com.aventstack.extentreports.ExtentTest;
    import org.openqa.selenium.*;
    import org.openqa.selenium.interactions.Actions;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;
    import org.testng.*;
    import org.testng.annotations.*;
    import pageObject.MSDATHealthFacility;
    import resources.Base;
    import resources.PageLoadUtils;

    import java.io.IOException;
    import java.time.Duration;

    public class MSDATHealthFacilities extends Base {
       public WebDriver driver;
       public WebDriverWait wait;

       public MSDATHealthFacility msdatHealthFacility;
       private ExtentTest test;


        @BeforeClass
        public void initializeDriver() throws IOException {
            this.driver = initializeWebDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
            driver.get(prop.getProperty("url"));

            // Use utility methods
            PageLoadUtils.waitForFullLoad(driver);  // Wait for full load
            PageLoadUtils.handleModals(driver);     // Handle modals
            msdatHealthFacility = new MSDATHealthFacility(driver);
        }


        @Test(priority = 0)
        public void messWithModal() throws InterruptedException {
            new WebDriverWait(driver, Duration.ofSeconds(240))
                    .until(ExpectedConditions.visibilityOf(msdatHealthFacility.getWhatsNewPopupClose()));
            clickWithRetry(msdatHealthFacility.getWhatsNewPopupClose());
            System.out.println("here is the first");

            new WebDriverWait(driver, Duration.ofSeconds(240))
                    .until(ExpectedConditions.visibilityOf(msdatHealthFacility.getTutorialSkipBtn()));
            clickWithRetry(msdatHealthFacility.getTutorialSkipBtn());
            System.out.println("here is the second");

            new WebDriverWait(driver, Duration.ofSeconds(240))
                    .until(ExpectedConditions.visibilityOf(msdatHealthFacility.getSectionGuideClose()));
            clickWithRetry(msdatHealthFacility.getSectionGuideClose());
            System.out.println("here is the final");
        }

        @Test(priority = 1)
        public void verifyHFacilityPage() throws InterruptedException {
            new WebDriverWait(driver, Duration.ofSeconds(240))
                    .until(ExpectedConditions.visibilityOf(msdatHealthFacility.getDashBoardSelectnDrpDwn()));
            msdatHealthFacility.getDashBoardSelectnDrpDwn().click();

            new WebDriverWait(driver, Duration.ofSeconds(240))
                    .until(ExpectedConditions.visibilityOf(msdatHealthFacility.getHealthFacility()));
            msdatHealthFacility.getHealthFacility().click();


            ///////*******///////
            //driver.switchTo().window(driver.getWindowHandles().stream().filter(handle -> !handle.equals(driver.getWindowHandle())).findFirst().get());


            String originalWindow = driver.getWindowHandle();
            new WebDriverWait(driver, Duration.ofSeconds(40))
                    .until(driver -> driver.getWindowHandles().size() > 1);

            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            ///////*******///////



            Thread.sleep(12000);
            String actualTitle = driver.getTitle();
            Assert.assertEquals(actualTitle, "MSDAT Nigeria | Health Facility", "Page title mismatch");
            System.out.println("here is initial");
            Thread.sleep(40000);
        }


        public void verifyHealthFacilityDashboardNav(){

        }

        @Test(priority = 2)
        public void verifyIndicatorSelect() throws InterruptedException {
            // Click input to open dropdown
            WebElement dropdownInput = msdatHealthFacility.getHFacilityIndicatorSelector();
            System.out.println("A");
            new WebDriverWait(driver, Duration.ofSeconds(240));
            wait.until(ExpectedConditions.elementToBeClickable(dropdownInput));
            dropdownInput.click();
            System.out.println("B");

            // Click the specific option (replace text with real value)
            WebElement option = msdatHealthFacility.getHFacilityIndicatorOption();
            System.out.println("D");
            new WebDriverWait(driver, Duration.ofSeconds(240));
            wait.until(ExpectedConditions.elementToBeClickable(option));
            option.click();
            System.out.println("This is achievable");
            new WebDriverWait(driver, Duration.ofSeconds(240));
            wait.until(ExpectedConditions.elementToBeClickable(msdatHealthFacility.getHFacilityIndicatorOption()));
            msdatHealthFacility.getHFacilityIndicatorOption().click();
            WebElement indicatorTable = driver.findElement(By.cssSelector("div[id='the-table'] div div[class='card-header d-flex justify-content-between border-bottom-0 align-items-center base_subCard']"));
            String indicatorTableHeader = indicatorTable.getText().toString();
            System.out.println(indicatorTableHeader);
            WebElement table  = driver.findElement(By.cssSelector("div[class='w-100 d-flex justify-content-between align-items-center position-relative p-1'] b"));
            Assert.assertTrue(indicatorTable.isDisplayed(), "Table is displayed");
            Assert.assertTrue(indicatorTableHeader.contains("Proportion of Health Facilities with Basic Equipment and related indicators (with year of latest values) across Nigeria"));
        }


        @Test(priority = 3)
        public void verifyUserPrintChart() throws InterruptedException {
            Thread.sleep(40000);
            new WebDriverWait(driver, Duration.ofSeconds(480))
                    .until(ExpectedConditions.visibilityOf(msdatHealthFacility.getSubTableMenu()));
            msdatHealthFacility.getSubTableMenu().click();
            System.out.println("I have it");
            WebElement printBtn = msdatHealthFacility.getPrintBtn();
            System.out.println("I'm on print btn");
            Assert.assertTrue(printBtn.isDisplayed());
            Assert.assertTrue(printBtn.isEnabled());
            System.out.println("this finishes it");
        }

        @Test(priority = 4)
        public void verifyZonalMapExpandBtn() throws InterruptedException {
           // Thread.sleep(10000);
            new WebDriverWait(driver, Duration.ofSeconds(480));
            wait.until(ExpectedConditions.elementToBeClickable(msdatHealthFacility.expandZonalMap()));
            msdatHealthFacility.expandZonalMap().click();
            System.out.println("zonal map");
            WebElement modalHeader = driver.findElement(By.id("__BVID__93___BV_modal_outer_"));
            System.out.println("did we get this done?");
            // Optionally print or validate text
            String headerText = modalHeader.getText();
            Assert.assertTrue(headerText.contains("Distribution of Proportion of Health Facilities with Basic Equipment across Nigeria"));
            System.out.println("essentially at the end");
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ESCAPE).perform();
        }




        @Test(priority = 5)
        public void verifyColumnChartExpand() throws InterruptedException {
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ESCAPE).perform();

            WebElement button = msdatHealthFacility.activateColChart();
            new WebDriverWait(driver, Duration.ofSeconds(480));
            wait.until(ExpectedConditions.elementToBeClickable(button));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            button.click();

            System.out.println("lever");
            WebElement btn = msdatHealthFacility.expandColChart();
            new WebDriverWait(driver, Duration.ofSeconds(240));
            wait.until(ExpectedConditions.elementToBeClickable(btn));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, -200);", btn); // Scrolls up by 200 pixels
         // js.executeScript("arguments[0].scrollIntoView(true);", btn);
            btn.click();

            System.out.println("now click");

            WebElement colModalHeader = driver.findElement(By.id("__BVID__83___BV_modal_content_") );

            System.out.println("making sense");

            String colHeaderText = colModalHeader.getText();
            Assert.assertTrue(colHeaderText.contains("Trend analysis of Proportion of Health Facilities with Basic Equipment across periods"));
            System.out.println(("on column chart progress"));
//            actions = new Actions(driver);
           actions.sendKeys(Keys.ESCAPE).perform();
            System.out.println("hello world");
        }


        @Test(priority = 6)
        public void verifyUserDownloadChart() throws InterruptedException {
           Actions actions = new Actions(driver);
           actions.sendKeys(Keys.ESCAPE).perform();
            System.out.println("we are in ");
            Thread.sleep(4000);
            WebElement tableOptn = msdatHealthFacility.getCharTabMenu();
            new WebDriverWait(driver, Duration.ofSeconds(40));
            wait.until(ExpectedConditions.elementToBeClickable(tableOptn));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, -300);", tableOptn); // Scrolls up by 300 pixels
           js.executeScript("arguments[0].scrollIntoView(true);", tableOptn);
            tableOptn.click();
          //  Thread.sleep(3000);
            WebElement dwnLdBtn = msdatHealthFacility.getDwnLoadBtn();
            new WebDriverWait(driver, Duration.ofSeconds(240));
            wait.until(ExpectedConditions.elementToBeClickable(dwnLdBtn));
           // dwnLdBtn.click();
            System.out.println("on the way to download");
            Assert.assertTrue(dwnLdBtn.isDisplayed());
            System.out.println("I'm on chart download btn");
            Assert.assertTrue(dwnLdBtn.isEnabled());
            System.out.println("download complete");
        }

        @Test(priority = 7)
        public void verifyZonalAnalysisTab() throws InterruptedException {
            Thread.sleep(40000);
            new WebDriverWait(driver, Duration.ofSeconds(240));
            wait.until(ExpectedConditions.elementToBeClickable(msdatHealthFacility.getZonalAnalysisTab()));
           msdatHealthFacility.getZonalAnalysisTab().click();
            System.out.println("we have it");
            Assert.assertTrue(msdatHealthFacility.getZonalAnalysisChart().isDisplayed());
            Assert.assertTrue(msdatHealthFacility.getZonalAnalysisMap().isDisplayed());
        }

        @Test(priority = 8)
        public void verifyZonalAnalysisPrintChart() throws InterruptedException {
            Thread.sleep(40000);
            new WebDriverWait(driver, Duration.ofSeconds(480));
            wait.until(ExpectedConditions.elementToBeClickable(msdatHealthFacility.getZonalAnalysisChartMenu()));
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
            Thread.sleep(40000);
            WebElement downLoadBtn = msdatHealthFacility.getZonalAnalysisDownloadChart();
            new WebDriverWait(driver, Duration.ofSeconds(240));
            wait.until(ExpectedConditions.elementToBeClickable(downLoadBtn));
            System.out.println("zonal analysis chart download");
            Assert.assertTrue(downLoadBtn.isDisplayed());
            System.out.println("zonal analysis chart download btn");
            Assert.assertTrue(downLoadBtn.isEnabled());
            System.out.println("zonal analysis PNG download complete");
        }


        @Test(priority = 10)
        public void verifyHFacMultiSourceCompareViewIndDataYrs() throws InterruptedException {
            new WebDriverWait(driver, Duration.ofSeconds(240));
            wait.until(ExpectedConditions.elementToBeClickable(msdatHealthFacility.getHFacMultiSourceCompareTab()));
            msdatHealthFacility.getHFacMultiSourceCompareTab().click();
            System.out.println("Multi Source Compare");
            Assert.assertTrue(msdatHealthFacility.getMultiSourceCompareIndicatorChart1().isDisplayed());
            Assert.assertTrue(msdatHealthFacility.getMultiSourceCompareIndicatorChart2().isDisplayed());
            Assert.assertTrue(msdatHealthFacility.getMultiSourceCompareIndicatorChart3().isDisplayed());
        }


        @Test(priority = 11)
        public void verifyHFacIndicatorCompare(){
            new WebDriverWait(driver, Duration.ofSeconds(240));
            wait.until(ExpectedConditions.elementToBeClickable(msdatHealthFacility.getHFacIndicatorCompareTab()));
            msdatHealthFacility.getHFacIndicatorCompareTab().click();
            System.out.println("a new dawn Indicator Ccompare");
        }

        @Test(priority = 12)
        public void verifyHFacDatasetIndicatorCompare(){
            new WebDriverWait(driver, Duration.ofSeconds(240));
            wait.until(ExpectedConditions.elementToBeClickable(msdatHealthFacility.getHFacDatasetCompare()));
            msdatHealthFacility.getHFacDatasetCompare().click();
            System.out.println("@ dataset compare");
        }

        @AfterClass
        public void tearDown() {

            //if (driver != null) driver.quit();
        }
    }