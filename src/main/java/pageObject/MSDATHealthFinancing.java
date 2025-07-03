    package pageObject;

    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;

    public class MSDATHealthFinancing {

        public WebDriver driver;

        public MSDATHealthFinancing msdatHealthFinancing;

        public MSDATHealthFinancing(WebDriver driver) {
            this.driver = driver;
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
