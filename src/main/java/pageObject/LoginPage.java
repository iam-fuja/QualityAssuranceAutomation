    package pageObject;

    import org.openqa.selenium.By;
    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;

    public class LoginPage {
        public WebDriver driver;

        public LoginPage(WebDriver driver){
            this.driver=driver;
        }

        private By skipButton = By.cssSelector("button.bg-white");

        public WebElement getSkipButton(){
            return driver.findElement(skipButton);
        }

        private By introJsExit = By.cssSelector("a[class='introjs-skipbutton']");
        private By tutorialExit = By.cssSelector("svg[plerdy-tracking-id='97028650701']");
        private By tutorialSkip = By.cssSelector("button[class='bg-white skip']");


        private By popUpSkipButton = By.cssSelector("[plerdy-tracking-id='28125588201']");
       // private By loginRegisterLink = By.cssSelector("span.d-none.d-md-inline");
        private By loginRegisterLink = By.cssSelector("[class='d-none d-md-inline']");

        private By username = By.cssSelector("input[placeholder='Username']");
        private By password = By.cssSelector("input[placeholder='Password']");
        private By loginButton = By.cssSelector("[plerdy-tracking-id='87167428501']");

        public WebElement getLoginRegisterLink(){
            return driver.findElement(loginRegisterLink);
        }

        public WebElement getUsername(){
            return driver.findElement(username);
        }

        public WebElement getPassword(){
            return driver.findElement(password);
        }

        public WebElement getLoginButton(){
            return driver.findElement(loginButton);
        }

        public WebElement getIntroJsExit(){
            return  driver.findElement(introJsExit);
        }

        public WebElement getTutorialExit(){
            return  driver.findElement(tutorialExit);
        }

        public WebElement getTutorialSkip(){
            return driver.findElement(tutorialSkip);
        }

    }
