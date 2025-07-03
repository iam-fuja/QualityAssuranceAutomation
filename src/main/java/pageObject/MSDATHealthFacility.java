    package pageObject;

    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;

    public class MSDATHealthFacility {
        public WebDriver driver;

        public MSDATHealthFacility(WebDriver driver) {
            this.driver = driver;
        }

        //main page objects
        private By dashboardSelectionDropDwn = By.cssSelector(".btn.btn-outline-primary.border-light.rounded-0 ");
        private By healthFacilityPage = By.linkText("Health Facilities");

        //Modal Pop-up objects
        private By whatsNewCloseBtn = By.cssSelector("div[class*=close-btn]");

        private By tutorialSkipBtn = By.cssSelector("button[class='bg-white skip']");

        private By sectionGuideCloseBtn = By.cssSelector("a[class*=introjs-skipbutton]");


        //indicator overview tab objects
        private By hfacIndicatorOverview = By.cssSelector("div[plerdy-tracking-id='20665876001']");

        private By hfacIndicatorDropdownOption = By.cssSelector("li[id=Indicator_Overview-0]");

        private By subTableMenu = By.xpath("(//*[name()='g'][@class='highcharts-exporting-group'])[2]");

        private By printBtn = By.xpath("//div[starts-with(@id, 'highcharts-')]//li[@class='highcharts-menu-item'][normalize-space()='Print chart']\n");

        private By downloadPNG = By.xpath("//li[@class='highcharts-menu-item' and contains(text(), 'Download PNG')]");

        private By chartTableMenu = By.xpath("//div[@id='highcharts-c2zo8tn-172']//*[name()='svg']//*[name()='g' and contains(@class,'highcharts')]//*[name()='g' and contains(@class,'highcharts')]//*[name()='rect' and contains(@class,'highcharts')]");

        private By chartDownloadPNG = By.xpath("//div[@id='highcharts-c2zo8tn-172']//li[@class='highcharts-menu-item'][normalize-space()='Download PNG image']");

        private  By zonalMapExpandBtn = By.cssSelector("div[id='stateBarChartComponent'] img");

        private By colChartActivationBtn = By.cssSelector(".btn.btn-sm.btn-outline-primary.active");

        private By colChartExpandBtn = By.cssSelector("div[class='iddc_wrapper confidenceRange_Intro position-relative'] img");


        //zonal analysis Tab elements
        private By zonalAnalysisTab = By.cssSelector("li[id='panel-1'] div[class='d-flex justify-content-between el-tit align-items-center']");
        //  xpath -  //div[normalize-space()='Zonal Analysis']
        public  WebElement getZonalAnalysisTab(){
            return driver.findElement(zonalAnalysisTab);
        }


        //zonal analysis chart and map elements
        private By zonalAnalysisChart = By.cssSelector("div[class='b-overlay-wrap position-relative main'] div[class='card-header d-flex justify-content-between border-bottom-0 align-items-center base_subCard']");

        private By zonalAnalysisMap = By.cssSelector("div[class='col-md-4'] div[class='card-header d-flex justify-content-between border-bottom-0 align-items-center base_subCard'] span:nth-child(2)");

        public WebElement getZonalAnalysisChart(){
            return driver.findElement(zonalAnalysisChart);
        }

        public WebElement getZonalAnalysisMap(){
            return driver.findElement(zonalAnalysisMap);
        }

        //zonal analysis print chart
        private By zonalAnalysisPrintChart = By.xpath("(//*[name()='path' and contains(@class, 'highcharts-button-symbol')])[1]");
                                                                    //div[@id='highcharts-aksvcc4-1132']//*[name()='path' and contains(@class, 'highcharts-button')]
        private By zonalAnalysisPrintBtn = By.cssSelector("div[class='container-fluid lessVisible mb-5'] li:nth-child(2)");

        public WebElement getZonalAnalysisChartMenu(){
            return driver.findElement(zonalAnalysisPrintChart);
        }

        public WebElement getZonalAnalysisPrintBtn(){
            return driver.findElement(zonalAnalysisPrintBtn);
        }


        //zonal analysis download chart
        private By zonalAnalysisDownloadChart = By.xpath("//li[normalize-space()='Download PNG image']");

        public WebElement getZonalAnalysisDownloadChart(){
            return driver.findElement(zonalAnalysisDownloadChart);
        }


        //indicator comparison
        private By hFacIndicatorCompareTab = By.xpath("//li[@id='panel-2']//div[contains(text(), 'Indicator Comparison')]");


        public WebElement getHFacIndicatorCompareTab(){
            return driver.findElement(hFacIndicatorCompareTab);
        }


        //Dataset comparison
        private By hFacDatasetComparison = By.xpath("//li[@id='panel-3']//div[contains(text(), 'Dataset Comparison')]");


        public WebElement getHFacDatasetCompare(){
            return driver.findElement(hFacDatasetComparison);
        }

        //multi-source comparison

        private By hFacMultiSourceCompareTab = By.xpath("//li[@id='panel-4']//div[contains(text(), 'Multi-Source Comparison')]");
        private By multiSourceCompareIndicatorChart1 = By.xpath("(//div[@class='dummy-row2 row']//p)[1]");
        private By multiSourceCompareIndicatorChart2 = By.xpath("(//div[@class='dummy-row2 row']//p)[2]");
        private By multiSourceCompareIndicatorChart3 = By.xpath("(//div[@class='dummy-row2 row']//p)[3]");


        public WebElement getHFacMultiSourceCompareTab(){
            return driver.findElement(hFacMultiSourceCompareTab);
        }

        public WebElement getMultiSourceCompareIndicatorChart1(){
            return driver.findElement(multiSourceCompareIndicatorChart1);
        }

        public WebElement getMultiSourceCompareIndicatorChart2(){
            return driver.findElement(multiSourceCompareIndicatorChart2);
        }

        public WebElement getMultiSourceCompareIndicatorChart3(){
            return driver.findElement(multiSourceCompareIndicatorChart3);
        }





        //main page selector methods
        public WebElement getDashBoardSelectnDrpDwn(){
            return driver.findElement(dashboardSelectionDropDwn);
        }

        public WebElement getHealthFacility(){
            return driver.findElement(healthFacilityPage);
        }



        //selector methods
        public WebElement getWhatsNewPopupClose(){
            return driver.findElement(whatsNewCloseBtn);
        }

        public WebElement getTutorialSkipBtn(){
            return driver.findElement(tutorialSkipBtn);
        }

        public WebElement getSectionGuideClose(){
            return driver.findElement(sectionGuideCloseBtn);
        }

        public WebElement getHFacilityIndicatorSelector(){
            return driver.findElement(hfacIndicatorOverview);
        }

        public WebElement getHFacilityIndicatorOption(){
            return driver.findElement(hfacIndicatorDropdownOption);
        }

        public WebElement getSubTableMenu(){
            return driver.findElement(subTableMenu);
        }

        public WebElement getPrintBtn(){
            return driver.findElement(printBtn);
        }

        public WebElement getDwnLoadBtn(){ return driver.findElement(downloadPNG);}

        public WebElement expandZonalMap(){
            return driver.findElement(zonalMapExpandBtn);
        }

        public WebElement activateColChart(){
            return driver.findElement(colChartActivationBtn);
        }

        public WebElement expandColChart(){
            return driver.findElement(colChartExpandBtn);
        }

        public WebElement getChartTableMenu(){
            return driver.findElement(chartTableMenu);
        }

        public WebElement getChartDwnLoadBtn(){
            return driver.findElement(chartDownloadPNG);
        }
    }
