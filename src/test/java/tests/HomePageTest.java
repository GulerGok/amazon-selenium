package tests;

import base.BaseTest;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;

import java.time.Duration;
import java.util.List;

public class HomePageTest extends BaseTest {

    @Test
    public void TC02_TestSearchSuggestions() {
        if (driver == null) {
            System.out.println("Driver null, test başlatılamadı!");
            return;
        }

        driver.get("https://www.amazon.com");
        HomePage homePage = new HomePage(driver);

        // Arama kutusuna 'laptop' yazma işlemi
        boolean isSearchSuccessful = homePage.enterSearchText("laptop");
        if (!isSearchSuccessful) {
            Assert.fail("Arama kutusuna metin girilemedi!");
            return; // Arama kutusuna metin girilemediyse testi sonlandırıyoruz.
        }

        try {
            // Arama önerilerini bekleyelim (WebDriverWait kullanımını güncelledik)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement suggestionsElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#nav-flyout-searchAjax .s-suggestion"))
            );

            // Arama önerilerinin görünür olup olmadığını kontrol et
            Assert.assertTrue(suggestionsElement.isDisplayed(), "Arama önerileri görünür olmalı");

        } catch (Exception e) {
            Assert.fail("Arama önerileri görünür olmalı: " + e.getMessage());
        }
    }

    @Test
    public void TC03_VerifyLanguageChange() {
        if (driver == null) {
            Assert.fail("Driver null, test başlatılamaz!");
        }

        driver.get("https://www.amazon.com");
        HomePage homePage = new HomePage(driver);

        try {
            // Dil değiştirme işlemi
            homePage.changeLanguageToSpanish();

            // Sayfa yüklendikten sonra URL'de dil değişikliğini kontrol et
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Bekleme süresi arttırıldı
            wait.until(ExpectedConditions.urlContains("language=es_US")); // URL'yi kontrol et

            // Sayfa içeriğinin İspanyolca olduğunu doğrula
            Assert.assertTrue(homePage.isPageInSpanish(), "Sayfa içeriği İspanyolca olmalı!");

        } catch (Exception e) {
            Assert.fail("Dil değiştirilirken bir hata oluştu: " + e.getMessage());
        }
    }

    @Test
    public void TC04_SignInPanelTest() {
        if (driver == null) {
            Assert.fail("Driver null, test başlatılamaz!");
        }

        driver.get("https://www.amazon.com");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // Daha esnek bir locator kullandık
            By accountListLocator = By.cssSelector("a[id='nav-link-accountList'], a[data-nav-role='signin']");

            WebElement accountMenu = wait.until(ExpectedConditions.presenceOfElementLocated(accountListLocator));

            // Hover işlemi
            Actions actions = new Actions(driver);
            actions.moveToElement(accountMenu).perform();

            // Tıklanabilir olmasını bekle ve tıkla
            wait.until(ExpectedConditions.elementToBeClickable(accountMenu)).click();

            // E-posta alanını bekle
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_email_login")));

            Assert.assertTrue(emailField.isDisplayed(), "Login paneli açılmadı!");

        } catch (Exception e) {
            Assert.fail("Login ekranı doğrulanamadı: " + e.getMessage());
        }
    }

    @Test
    public void TC05_LocationUpdateChangesContent() {
        if (driver == null) {
            Assert.fail("Driver null, test başlatılamaz!");
        }

        driver.get("https://www.amazon.com");
        HomePage homePage = new HomePage(driver);

        try {
            boolean isUpdated = homePage.changeZipCode("10001");
            Assert.assertTrue(isUpdated, "Konum güncellenmemiş gibi görünüyor!");

        } catch (Exception e) {
            Assert.fail("Lokasyon değiştirme sırasında hata: " + e.getMessage());
        }
    }

    @Test
    public void TC06_ResponsiveDesignTest() {
        if (driver == null) {
            Assert.fail("Driver null, test başlatılamaz!");
        }

        try {
            // Sayfayı aç
            driver.get("https://www.amazon.com");

            // Mobil boyuta getir (örnek: iPhone 12 ekran boyutu)
            Dimension mobileSize = new Dimension(390, 844);
            driver.manage().window().setSize(mobileSize);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Sayfa yüklenmesini bekle
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));

            // Hamburger menü simgesi kontrolü (Mobilde gözükmeli)
            WebElement hamburgerMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-hamburger-menu")));
            Assert.assertTrue(hamburgerMenu.isDisplayed(), "Hamburger menü mobil görünümde görünmüyor!");

            // Sayfa tasarımı bozulmamış mı kontrol (örnek olarak header bölümü)
            WebElement header = driver.findElement(By.id("navbar"));
            Assert.assertTrue(header.isDisplayed(), "Header bölümü bozulmuş olabilir!");

            System.out.println("Mobil görünüm testi başarıyla tamamlandı.");

        } catch (Exception e) {
            Assert.fail("Mobil görünüm testinde hata oluştu: " + e.getMessage());
        }
    }

    @Test
    public void TC07_VerifyCarouselArrowsFunctionality() {
        driver.get("https://www.amazon.com");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // Tarayıcıyı maximize et
            driver.manage().window().maximize();

            // Karusel yüklenene kadar bekle
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("gw-desktop-herotator")));

            // İlk görselin alt metni
            WebElement firstImage = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("#gw-desktop-herotator .a-carousel-card img[alt]")));
            String altBefore = firstImage.getAttribute("alt");
            System.out.println("İlk görselin alt metni (önce): " + altBefore);

            // Sağ oka tıkla
            WebElement rightArrow = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".a-carousel-goto-nextpage")));
            js.executeScript("arguments[0].scrollIntoView(true);", rightArrow);
            rightArrow.click();
            System.out.println("Sağ ok simgesi tıklandı.");

            // Yeni görselin yüklenmesini bekle
            Thread.sleep(3000);
            WebElement afterRightImage = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("#gw-desktop-herotator .a-carousel-card img[alt]")));
            String altAfterRight = afterRightImage.getAttribute("alt");
            System.out.println("Görselin alt metni (sağ ok sonrası): " + altAfterRight);

            // Sağ ok sonrası alt metin farklı olmalı
            Assert.assertNotEquals(altBefore, altAfterRight, "Sağ oka tıklanmasına rağmen görsel değişmedi!");

            // Sol oka tıkla
            WebElement leftArrow = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".a-carousel-goto-prevpage")));
            js.executeScript("arguments[0].scrollIntoView(true);", leftArrow);
            leftArrow.click();
            System.out.println("Sol ok simgesi tıklandı.");

            // Tekrar eski görsel yükleniyor mu kontrol et
            Thread.sleep(3000);
            WebElement afterLeftImage = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("#gw-desktop-herotator .a-carousel-card img[alt]")));
            String altAfterLeft = afterLeftImage.getAttribute("alt");
            System.out.println("Görselin alt metni (sol ok sonrası): " + altAfterLeft);

            // Sol ok sonrası ilk görsel geri gelmiş olmalı
            Assert.assertEquals(altBefore, altAfterLeft, "Sol oka tıklanmasına rağmen eski görsel geri gelmedi!");

        } catch (TimeoutException e) {
            Assert.fail("Karusel öğesi veya ok simgeleri yüklenemedi: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Test sırasında hata oluştu: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

}