    import org.openqa.selenium.*;
    import org.openqa.selenium.chrome.ChromeDriver;
    import org.openqa.selenium.chrome.ChromeOptions;
    import org.openqa.selenium.interactions.Actions;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.WebDriverWait;
    import org.testng.annotations.AfterClass;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.Test;
    import pageObject.LoginPage;
    import pageObject.MSDATHealthOutcomesServiceCoverageDashboard;
    import resources.Base;

    import java.io.IOException;
    import java.time.Duration;
    import java.util.HashMap;
    import java.util.Map;

    public class UserLogin extends Base {
        public WebDriver driver;
        public LoginPage loginPage;
        public Actions action;
        public MSDATHealthOutcomesServiceCoverageDashboard msdatHealthOutcomesServiceCoverageDashboard;


        @BeforeClass
        public void initializeDriver() throws IOException {
            this.driver = initializeWebDriver();

            driver.get(prop.getProperty("url"));
            driver.manage().window().maximize();

            loginPage = new LoginPage(driver);
            msdatHealthOutcomesServiceCoverageDashboard = new MSDATHealthOutcomesServiceCoverageDashboard(driver);
            action = new Actions(driver);

        }


        @Test
        public void Login() throws InterruptedException {

            //Calling login credentials from config files
            String username = prop.getProperty("user_username");
            String password = prop.getProperty("user_password");





            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

            try {
                // Wait for modal backdrop to appear first (common pattern)
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.modal-content.noShadow")));
                System.out.println("MakA");

                // Now handle the modal content
                WebElement modal = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("div.modal-content.noShadow")));
                System.out.println("Makanjuola");

                // Try to close it - different methods
                try {
                    // Method 1: Click close button
                    modal.findElement(By.cssSelector("#__BVID__107___BV_modal_body_ > div.onboarding__bg > div.onboarding__body > button.bg-white.skip")).click();
                    System.out.println("Adegunwa");
                } catch (NoSuchElementException e) {
                    // Method 2: Click outside modal
                    action.moveByOffset(10, 10).click().perform();
                    System.out.println("Imole");
                }

            } catch (TimeoutException e) {
                System.out.println("No modal appeared within timeout");
            }

            Thread.sleep(8000);
            System.out.println("Grace");

            WebElement button = driver.findElement(By.cssSelector(
                    "div.modal-content.noShadow"));
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", button);
            System.out.println("Iyanulolorun");

            Thread.sleep(8000);

            WebElement buttonn = driver.findElement(By.cssSelector(
                    "div.introjs-overlay"));
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", buttonn);
            System.out.println("Oyenike");




            msdatHealthOutcomesServiceCoverageDashboard.chooseDashboard().click();
           // loginPage.getLoginRegisterLink().click();
            System.out.println("Blessings");
            msdatHealthOutcomesServiceCoverageDashboard.hoscDashboardLink().click();
            System.out.println("Hosana");

//            loginPage.getUsername().sendKeys(username);
//            loginPage.getPassword().sendKeys(password);
//            loginPage.getLoginButton().click();
        }


        @AfterClass
        public void tearDown(){

            //driver.quit();
        }

    }
