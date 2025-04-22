package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;

import java.time.Duration;

public class HomePageTest extends BaseTest {

    @Test
    public void TC02_testSearchSuggestions() {
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
    public void TC04_signInPanelTest() {
        if (driver == null) {
            Assert.fail("Driver null, test başlatılamaz!");
        }

        driver.get("https://www.amazon.com");
        HomePage homePage = new HomePage(driver);

        try {
            // Giriş butonuna tıklayın ve login ekranının açıldığını doğrulayın
            boolean isSignInPanelOpened = homePage.clickSignInAndVerifyPanel();  // Bu metodun true/false döndürdüğünden emin olun

            // Panelin açıldığını doğrulayın
            Assert.assertTrue(isSignInPanelOpened, "Login paneli açılmadı!");

            // WebDriverWait nesnesini oluşturun (Selenium 4 ile uyumlu)
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Email alanının görünür olduğunu doğrula
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_email")));
            Assert.assertTrue(emailField.isDisplayed(), "Email alanı görünmüyor.");

            // Continue butonunun görünür olduğunu doğrula
            WebElement continueButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
            Assert.assertTrue(continueButton.isDisplayed(), "Continue butonu görünmüyor.");

        } catch (Exception e) {
            Assert.fail("Login ekranı doğrulanamadı: " + e.getMessage());
        }
    }

}
