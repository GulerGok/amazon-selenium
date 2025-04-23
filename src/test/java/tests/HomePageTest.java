package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;

import java.time.Duration;

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
    public void TC04_signInPanelTest() {
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
}
