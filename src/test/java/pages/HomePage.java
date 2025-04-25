package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // Locator'lar
    private By logo = By.id("nav-logo-sprites");
    private By searchBox = By.id("twotabsearchtextbox");
    private By promoBanner = By.id("desktop-banner"); // Bu ID zamanla değişebilir
    private By footer = By.id("navFooter");
    private By captchaBox = By.id("captchacharacters");  // Captcha alanı ID'si
    private By languageMenu = By.id("icp-nav-flyout");
    private By carouselItems = By.cssSelector("#gw-desktop-herotator .a-carousel-card");
    private By rightArrow = By.cssSelector("#gw-desktop-herotator .a-carousel-goto-nextpage");
    private By leftArrow = By.cssSelector("#gw-desktop-herotator .a-carousel-goto-prevpage");
    private By backToTopButton = By.id("navBackToTop");  // Back to Top butonunun id'si

    // Actions / Methods
    public boolean isCaptchaPage() {
        try {
            WebElement captcha = driver.findElement(captchaBox);
            return captcha.isDisplayed(); // Eğer Captcha varsa true döner
        } catch (Exception e) {
            return false; // Eğer Captcha yoksa false döner
        }
    }

    // Elementlerin görünür olduğunu kontrol etme (WebDriverWait eklenmiş)
    public boolean isElementVisible(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Sayfa öğelerinin görünürlük kontrolü
    public boolean isLogoVisible() {
        return isElementVisible(logo);
    }

    public boolean isSearchBoxVisible() {
        return isElementVisible(searchBox);
    }

    public boolean isPromoBannerVisible() {
        return isElementVisible(promoBanner);
    }

    public boolean isFooterVisible() {
        return isElementVisible(footer);
    }

    // Arama kutusuna metin girme
    public boolean enterSearchText(String searchQuery) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(searchBox));  // Arama kutusunun tıklanabilir olmasını bekle
            WebElement searchBox = driver.findElement(this.searchBox);
            searchBox.sendKeys(searchQuery);
            return true;  // Başarıyla metin girildi
        } catch (Exception e) {
            System.out.println("Arama kutusuna metin girerken bir hata oluştu: " + e.getMessage());
            return false;  // Hata oluştuysa false döndür
        }
    }

    // Dil değiştirme işlemi
    public void changeLanguageToSpanish() {
        try {
            WebElement languageMenuElement = wait.until(ExpectedConditions.visibilityOfElementLocated(languageMenu));
            Actions actions = new Actions(driver);
            actions.moveToElement(languageMenuElement).pause(Duration.ofSeconds(1)).perform(); // Menüye hover

            // "es-US" dil seçeneğine tıklama
            WebElement espanolOptionElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[lang='es-US']"))); // "es-US" dil seçeneği
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", espanolOptionElement); // JavaScript ile tıklama

            // Sayfanın yüklenmesini bekleyin
            wait.until(ExpectedConditions.urlContains("language=es_US")); // URL'deki dil parametresi
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Mevcut URL: " + currentUrl);

            // URL'yi doğrulama
            if (!currentUrl.contains("language=es_US")) {
                throw new RuntimeException("Dil değişikliği yapılmadı. URL: " + currentUrl);
            }

        } catch (Exception e) {
            throw new RuntimeException("Dil değiştirilemedi: " + e.getMessage());
        }
    }

    // Sayfanın İspanyolca olduğunu doğrulama
    public boolean isPageInSpanish() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
            String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();

            return bodyText.contains("todo") ||
                    bodyText.contains("ofertas del día") ||
                    bodyText.contains("listas") ||
                    bodyText.contains("prime video") ||
                    bodyText.contains("tarjetas de regalo") ||
                    bodyText.contains("servicio al cliente") ||
                    bodyText.contains("vender");

        } catch (Exception e) {
            System.out.println("İspanyolca kontrolü sırasında hata oluştu: " + e.getMessage());
            return false;
        }
    }

    //Giriş panelinin açıldığını doğrula
    public boolean clickSignInAndVerifyPanel() {
        try {
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-link-accountList")));
            signInButton.click();

            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_email")));
            return emailField.isDisplayed();
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
            return false;
        }
    }

    // Lokasyon değiştir ve içeriklerin güncellendiğini doğrula
    public boolean changeZipCode(String zipCode) {
        try {
            driver.get("https://www.amazon.com/?language=en_US");
            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

            WebElement deliverToButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nav-global-location-popover-link")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", deliverToButton);
            wait.until(ExpectedConditions.elementToBeClickable(deliverToButton)).click();

            WebElement postalCodeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("GLUXZipUpdateInput")));
            postalCodeField.clear();
            postalCodeField.sendKeys(zipCode);

            WebElement applyButton = driver.findElement(By.cssSelector("#GLUXZipUpdate .a-button-input"));
            applyButton.click();

            wait.until(ExpectedConditions.invisibilityOf(postalCodeField));
            driver.navigate().refresh();

            WebElement locationTextElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("glow-ingress-line2")));
            String updatedLocationText = locationTextElement.getText();
            System.out.println("Güncellenmiş Lokasyon: '" + updatedLocationText + "'");

            return updatedLocationText.toLowerCase().replaceAll("\\s+", "").contains(zipCode.toLowerCase().replaceAll("\\s+", ""));
        } catch (Exception e) {
            System.out.println("Lokasyon değiştirilirken hata oluştu: " + e.getMessage());
            return false;
        }
    }

    // Mobil görünümde hamburger menüsünün görünürlüğünü kontrol et
    public boolean isHamburgerMenuVisible() {
        try {
            WebElement hamburgerMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-hamburger-menu")));
            return hamburgerMenu.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Karuseldeki ürünleri döner
    public List<WebElement> getCarouselItems() {
        return driver.findElements(carouselItems);
    }

    // İlk karusel öğesinin text'ini döner
    public String getFirstCarouselItemText() {
        try {
            WebElement firstItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#gw-desktop-herotator .a-carousel-card")));
            return firstItem.getText();
        } catch (Exception e) {
            System.out.println("Karusel öğesi alınamadı: " + e.getMessage());
            return "";
        }
    }

    // Sağ oka tıklar
    public void clickRightArrow() {
        WebElement rightArrow = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='gw-desktop-herotator']//a[@aria-label='Next']") // XPath'i güncelledik
        ));
        rightArrow.click();
    }

    // Sol oka tıklar
    public void clickLeftArrow() {
        WebElement leftArrow = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='gw-desktop-herotator']//a[@aria-label='Previous']") // XPath'i güncelledik
        ));
        leftArrow.click();
    }

    // İlk banner’a tıkla ve href bilgisini döndür
    public String clickFirstBannerAndReturnHref() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Banner'ı CSS selector ile bul
            WebElement firstBanner = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("#gw-desktop-herotator a.a-link-normal[href]")  // Banner'a tıklama için selector
            ));

            String href = firstBanner.getAttribute("href");

            // JavaScript ile tıklama – bazı durumlarda normal click çalışmayabilir
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstBanner);

            return href;
        } catch (Exception e) {
            System.out.println("Banner'a tıklanamadı: " + e.getMessage());
            return null;
        }
    }

    // Sayfa altına kaydırma ve Back to Top butonuna tıklama
    public void scrollToBottomAndClickBackToTop() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // Sayfanın altına kaydır
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

            // Back to Top butonunun görünür hale gelmesini bekle
            wait.until(ExpectedConditions.visibilityOfElementLocated(backToTopButton));

            // Back to Top butonuna tıklayalım
            WebElement backToTopBtn = driver.findElement(backToTopButton);
            if (backToTopBtn.isDisplayed()) {
                backToTopBtn.click();
            }
        } catch (Exception e) {
            System.out.println("Back to Top butonuna tıklanamadı: " + e.getMessage());
        }
    }

    // Sayfa üst kısmına dönüp dönmediğini doğrulama
    public boolean isAtTop() {
        try {
            WebElement logoElement = wait.until(ExpectedConditions.visibilityOfElementLocated(logo));
            return logoElement.isDisplayed();
        } catch (Exception e) {
            System.out.println("Sayfa üst kısmına dönülmedi: " + e.getMessage());
            return false;
        }
    }

    // Hamburger menüsüne tıklama
    public void openHamburgerMenu() {
        WebElement hamburgerMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("nav-hamburger-menu")
        ));
        hamburgerMenu.click();
    }

    // Kategorilerin açıldığını kontrol etme
    public boolean areCategoriesVisible() {
        try {
            WebElement categoriesList = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("hmenu-content")
            ));
            return categoriesList.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    //Footer'daki linklerin yönlendirilmesini doğrulama
    public boolean verifyFooterLinks(WebDriver driver, int maxLinksToTest) {
        boolean allPassed = true; // En sonda dönülecek genel sonuç
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(1000);

            List<WebElement> footerLinks = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#navFooter a[href]"))
            );

            System.out.println("🔍 Bulunan footer bağlantı sayısı: " + footerLinks.size());
            if (footerLinks.isEmpty()) return false;

            int testCount = Math.min(maxLinksToTest, footerLinks.size());

            for (int i = 0; i < testCount; i++) {
                try {
                    WebElement link = footerLinks.get(i);
                    String linkText = link.getText().trim();
                    String href = link.getAttribute("href");

                    System.out.println("\n👉 Test edilen link [" + (i + 1) + "]: " + linkText + " | URL: " + href);

                    driver.navigate().to(href);
                    wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));

                    String currentUrl = driver.getCurrentUrl();
                    String pageTitle = driver.getTitle().toLowerCase();
                    System.out.println("✅ Yönlendirilen URL: " + currentUrl);
                    System.out.println("📄 Sayfa başlığı: " + pageTitle);

                    if (pageTitle.contains("404") || pageTitle.contains("not found") || pageTitle.contains("error")) {
                        System.out.println("❌ Hatalı sayfa yüklendi: " + pageTitle);
                        allPassed = false;
                    }

                } catch (Exception innerEx) {
                    System.out.println("⚠️ Link testinde hata oluştu: " + innerEx.getMessage());
                    allPassed = false;
                }

                // Geri dön ve footer'ı tekrar bul
                driver.navigate().back();
                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(1000);

                // DOM yeniden yüklendiğinden linkleri tekrar al
                footerLinks = wait.until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("#navFooter a[href]"))
                );
            }

        } catch (Exception e) {
            System.out.println("🚨 Footer link testi genel hata: " + e.getMessage());
            return false;
        }

        return allPassed;
    }

}
