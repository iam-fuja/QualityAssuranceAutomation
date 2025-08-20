    package pageObject;

    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.interactions.Actions;

    public class MSDATHealthFinance {

        public WebDriver driver;

        public MSDATHealthFinance(WebDriver driver) {
            this.driver = driver;
        }

        //main page objects
        private By financePage = By.linkText("Health Financing");

        public WebElement getMSDATFinancePage(){
            return driver.findElement(financePage);
        }



        //main page objects

        private By healthFacilityPage = By.linkText("Health Facilities");

        //Modal Pop-up objects
        private By whatsNewCloseBtn = By.cssSelector("div[class*=close-btn]");
        public WebElement getWhatsNewPopupClose() { return driver.findElement(whatsNewCloseBtn);
        }

        private By tutorialSkipBtn = By.cssSelector("button[class='bg-white skip']");
        public WebElement getTutorialSkipBtn() { return driver.findElement(tutorialSkipBtn);
        }

        private By sectionGuideCloseBtn = By.cssSelector("a[class*=introjs-skipbutton]");
        public WebElement getSectionGuideClose() { return driver.findElement(sectionGuideCloseBtn);
        }

        private By dashboardSelectionDropDwn = By.cssSelector(".btn.btn-outline-primary.border-light.rounded-0 ");

        public WebElement getDashBoardSelectnDrpDwn() {
            return driver.findElement(dashboardSelectionDropDwn);
        }



        private By HFDashboardLink = By.linkText("Health Finance");

        private By HFIndicatorOverviewBtn = By.linkText("indicator overview");

        private By hfIndicatorDropdown = By.cssSelector("input[id='Indicator_Overview']");




        public WebElement getHFDashboardLink(){
            return driver.findElement(HFDashboardLink);
        }

        public WebElement getHFIndicatorOverview(){
            return driver.findElement(HFIndicatorOverviewBtn);
        }

        public WebElement getHFIndicatorDropdown(){
            return driver.findElement(hfIndicatorDropdown);
        }



    }
