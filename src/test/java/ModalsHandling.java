
        import org.openqa.selenium.*;
        import org.openqa.selenium.interactions.Actions;
        import org.openqa.selenium.support.ui.ExpectedConditions;
        import org.openqa.selenium.support.ui.WebDriverWait;
        import resources.Base;

        import java.time.Duration;
        import java.util.List;

        public class ModalsHandling extends Base {

                WebDriver driver;
                WebDriverWait wait;

                public ModalsHandling(WebDriver driver) {
                        this.driver = driver;
                        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                }

                public void closeAllVisibleModals() {
                        try {
                                // Target your custom modals
                                List<WebElement> modals = driver.findElements(By.cssSelector("div.datasource-container"));

                                for (WebElement modal : modals) {
                                        try {
                                                if (modal.isDisplayed()) {
                                                        wait.until(ExpectedConditions.visibilityOf(modal));

                                                        // Find and click the close button inside this modal
                                                        WebElement closeBtn = modal.findElement(By.cssSelector("div.close-btn"));
                                                        wait.until(ExpectedConditions.elementToBeClickable(closeBtn));
                                                        closeBtn.click();

                                                        // Optional: wait for modal to close
                                                        wait.until(ExpectedConditions.invisibilityOf(modal));
                                                }
                                        } catch (NoSuchElementException e) {
                                                System.out.println("No close button found for a modal, attempting fallback.");
                                                clickOutsideModal();
                                        } catch (ElementClickInterceptedException | TimeoutException e) {
                                                System.out.println("Modal close button blocked or timed out. Attempting fallback.");
                                                clickOutsideModal();
                                        }
                                }

                        } catch (Exception e) {
                                System.out.println("Error while handling modals: " + e.getMessage());
                        }
                }

                private void clickOutsideModal() {
                        try {
                                // Click outside the modal — attempt to click body or a backdrop if present
                                WebElement backdropOrBody = driver.findElement(By.cssSelector("body"));
                                new Actions(driver).moveToElement(backdropOrBody, 10, 10).click().perform();
                        } catch (Exception e) {
                                System.out.println("Fallback to click outside modal failed: " + e.getMessage());
                        }
                }
        }
