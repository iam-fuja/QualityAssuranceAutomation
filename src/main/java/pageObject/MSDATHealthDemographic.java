    package pageObject;

    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;

    public class MSDATDemographic {
        public WebDriver driver;

        public MSDATDemographic(WebDriver driver) {
            this.driver = driver;
        }



        //main page objects
        private By demographicPage = By.linkText("Demographics");

        public WebElement getMSDATDemographicsPage(){
            return driver.findElement(demographicPage);
        }



        //indicator dropdown selector
        private By demographIndiSelect = By.cssSelector("div[plerdy-tracking-id='96834783701']");

        private By demographicTableHeader = By.cssSelector("div[id='the-table'] div div[class='card-header d-flex justify-content-between border-bottom-0 align-items-center base_subCard']");

        public WebElement getDemographicsIndicatorSelector(){
            return driver.findElement(demographIndiSelect);
        }

        public WebElement getDemographicsTableHeader(){
            return driver.findElement(demographicTableHeader);
        }


        //indicator dropdown option
        private By demographicIndicatorDropdownOption = By.cssSelector("li[id=Indicator_Overview-0]");

        public WebElement getDemographicIndicatorOption(){
            return driver.findElement(demographicIndicatorDropdownOption);
        }



        //Location selection and Location dropdown option
        private By demographicLocationDropdown = By.cssSelector("div[plerdy-tracking-id='51624698901']");

        private By demographicLocationDropdownOption = By.cssSelector("li[plerdy-tracking-id='87476130401']");

        public WebElement getDemographicLocationDropdown(){
            return driver.findElement(demographicLocationDropdown);
        }

        public WebElement getDemographicLocationDropdownOption(){
            return driver.findElement(demographicLocationDropdownOption);
        }



        //more info
        private By infoIconBtn = By.cssSelector("td[class='align-middle table-info-icon'] div[class='d-flex justify-content-center'] svg path");


        //private By infoCLoseBtn = By.cssSelector(".btn.btn-danger.work-sans");
        private By infoCLoseBtn = By.cssSelector("//div[@class='modal-title']");

        public WebElement getDemographicInfoIcon (){
           return driver.findElement(infoIconBtn);
        }

        public WebElement getCloseInfoModalPage(){
            return driver.findElement(infoIconBtn);
        }

        //Indicator Overview Print Chart
        private By demographicIndicatorSubMenu = By.xpath("(//*[name()='g'][@class='highcharts-exporting-group'])[1]");

       // private By demographicIndSubMenuPrintBtn = By.cssSelector("div[class='col-md-8'] li:nth-child(2)");
       // private By demographicIndSubMenuPrintBtn = By.cssSelector(" div[class='col-md-8'] li:nth-child(2)");
       private By demographicIndSubMenuPrintBtn = By.xpath("//div[starts-with(@id, 'highcharts-')]//li[@class='highcharts-menu-item'][normalize-space()='Print chart']");


        public WebElement getDemoIndSubMenu(){
            return driver.findElement(demographicIndicatorSubMenu);
        }

        public WebElement getDemoIndSubMenuPrintBtn(){
            return driver.findElement(demographicIndSubMenuPrintBtn);
        }







    }
