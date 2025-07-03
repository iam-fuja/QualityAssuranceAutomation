    package resources;


    import org.openqa.selenium.*;
    import org.openqa.selenium.support.ui.*;
    import java.time.Duration;
    import java.util.List;

    public class PageLoadUtils {
        private static final Duration GLOBAL_TIMEOUT = Duration.ofSeconds(240);
        private static final Duration POLLING_INTERVAL = Duration.ofMillis(500);

        // Wait for full page load (DOM + JS)
        public static void waitForFullLoad(WebDriver driver) {
            WebDriverWait wait = new WebDriverWait(driver, GLOBAL_TIMEOUT);
            wait.pollingEvery(POLLING_INTERVAL);

            // 1. Wait for document.readyState = "complete"
            wait.until(d -> ((JavascriptExecutor) d)
                    .executeScript("return document.readyState").equals("complete"));

            // 2. Wait for jQuery/AJAX (if applicable)
            if ((Boolean) ((JavascriptExecutor) driver)
                    .executeScript("return window.jQuery != undefined")) {
                wait.until(d -> ((JavascriptExecutor) d)
                        .executeScript("return jQuery.active == 0"));
            }
        }

        // Handle common modals/popups
        public static void handleModals(WebDriver driver) {
            closeModalIfPresent(driver, "WhatsNew",
                    By.cssSelector("div.modal-content"),
                    By.cssSelector("div[class*='close-btn'], button.close"));
        }

        private static void closeModalIfPresent(WebDriver driver, String modalName,
                                                By modalLocator, By closeButtonLocator) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, GLOBAL_TIMEOUT);
                List<WebElement> modals = driver.findElements(modalLocator);

                if (!modals.isEmpty()) {
                    WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(modalLocator));
                    WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                            modal.findElement(closeButtonLocator)));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeBtn);
                    wait.until(ExpectedConditions.invisibilityOf(modal));
                }
            } catch (Exception e) {
                System.out.println(modalName + " modal handling skipped: " + e.getMessage());
            }
        }
    }