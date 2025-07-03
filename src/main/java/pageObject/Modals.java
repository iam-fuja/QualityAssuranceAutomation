    package pageObject;

    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;

    public class Modals {
        public WebDriver driver;

        public Modals(WebDriver driver) {
            this.driver = driver;
        }

        private By whatsNewCloseBtn = By.cssSelector("div[class*=close-btn]");


        private  By swapMinisterialDashboard = By.cssSelector("span[plerdy-tracking-id='82506238501']");

        private By quarterlyPerformAssessMinisterialDashboard = By.cssSelector("span[plerdy-tracking-id='82506238502']");



        public WebElement getWhatsNewCloseBtn(){
            return driver.findElement(whatsNewCloseBtn);
        }

        public WebElement getSwapMinisterialDashboard(){
            return driver.findElement(swapMinisterialDashboard);
        }

        public WebElement getQuarterlyPerformAssessMinsDashboard(){
            return driver.findElement(quarterlyPerformAssessMinisterialDashboard);
        }




    }
