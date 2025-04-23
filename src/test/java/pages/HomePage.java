package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Arama önerilerinin görünür olduğunu kontrol et
    public boolean isSearchSuggestionVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement suggestion = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("#nav-flyout-searchAjax .s-suggestion")));
            return suggestion.isDisplayed();
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

    public boolean enterSearchText(String searchQuery) {
        try {
            // Sayfanın tamamen yüklenmesini bekle
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(By.id("twotabsearchtextbox")));  // Arama kutusunun tıklanabilir olmasını bekle

            // Arama kutusunu bul ve metni gir
            WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
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
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // 1. Language menüsüne hover yap
            WebElement languageMenuElement = wait.until(ExpectedConditions.visibilityOfElementLocated(languageMenu));
            Actions actions = new Actions(driver);
            actions.moveToElement(languageMenuElement).pause(Duration.ofSeconds(1)).perform(); // Menüye hover

            // 2. İspanyolca dil seçeneğine tıklama
            WebElement espanolOptionElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[lang='es-US']"))); // "es-US" dil seçeneği

            // JavaScript ile tıklama
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", espanolOptionElement); // JavaScript ile tıklama

            // Sayfanın yüklenmesini bekleyin
            WebDriverWait waitForUrlChange = new WebDriverWait(driver, Duration.ofSeconds(10));
            waitForUrlChange.until(ExpectedConditions.urlContains("language=es_US")); // URL'deki dil parametresi

            // Sayfa URL'sini yazdırarak kontrol edin
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
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

            // Body metnini al ve küçük harfe çevir
            String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();

            // İspanyolca olabilecek ifadelerle kontrol et
            return  bodyText.contains("todo") ||
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
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));  // Bekleme süresi

            // Giriş butonuna tıklamadan önce öğenin tıklanabilir olduğundan emin olun
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-link-accountList")));
            signInButton.click();  // Tıklama işlemi

            // Email alanının görünür olup olmadığını kontrol et
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_email")));  // Email alanı görünüyor mu?
            return emailField.isDisplayed();  // Eğer email alanı görünüyorsa true döndür
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
            return false;  // Eğer tıklama veya öğe kontrolü başarısız olursa false döndür
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

    // Başlık (header) öğesinin görünür olduğunu doğrula
    public boolean isHeaderVisible() {
        try {
            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("navbar")));
            return header.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
