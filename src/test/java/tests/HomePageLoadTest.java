package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;

public class HomePageLoadTest extends BaseTest {

    @Test
    public void TC01_VerifyHomepageMainComponents() {
        // Eğer driver null ise, test hemen durur ve hata mesajı yazdırılır
        if (driver == null) {
            System.out.println("Driver null, test başlatılamadı!");
            return; // Testi durdur
        }

        driver.get("https://www.amazon.com");

        HomePage homePage = new HomePage(driver);

        // Ana bileşenleri doğrulama
        Assert.assertTrue(homePage.isLogoVisible(), "Amazon logosu görünmüyor.");
        Assert.assertTrue(homePage.isSearchBoxVisible(), "Arama kutusu görünmüyor.");
        Assert.assertTrue(homePage.isPromoBannerVisible(), "Promo/banner alanı görünmüyor.");
        Assert.assertTrue(homePage.isFooterVisible(), "Footer alanı görünmüyor.");
    }
}
