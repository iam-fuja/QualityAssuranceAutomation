    package pageObject;

    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;

    public class MSDATHealthOutcomesServiceCoverageDashboard {

        public WebDriver driver;

        public MSDATHealthOutcomesServiceCoverageDashboard(WebDriver driver) {
            this.driver = driver;
        }

        private By healthOutcomesServiceCoverageHead = By.cssSelector("h2[class='main-text d-inline-block']");

        private By selectDashboard =  By.cssSelector("button[class='btn btn-outline-primary border-light rounded-0']");

        private By healthOutcomesServiceCoverageLink = By.linkText("Health Outcomes and Service Coverage");







        public WebElement hoscTitle(){
            return driver.findElement(healthOutcomesServiceCoverageHead);
        }

        public WebElement chooseDashboard(){
            return driver.findElement(selectDashboard);
        }

        public WebElement hoscDashboardLink(){
            return driver.findElement(healthOutcomesServiceCoverageLink);
        }

    }
