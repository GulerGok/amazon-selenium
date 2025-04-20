package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;

public class AmazonTest {

    WebDriver driver;

    @BeforeTest
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/Program Files/JetBrains/webdrivers/chromedriver.exe");  // ChromeDriver'ın bulunduğu yeri belirtin
        driver = new ChromeDriver();
    }

    @Test
    public void searchTest() {
        // Amazon Türkiye'yi aç
        driver.get("https://www.amazon.com.tr");

        // Arama kutusuna eriş
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));

        // Arama kutusuna 'laptop' yaz ve arama yap
        searchBox.sendKeys("laptop");
        searchBox.submit();

        // Sayfa başlığını al
        String title = driver.getTitle();

        // Sayfa başlığını konsola yazdır
        System.out.println("Sayfa başlığı: " + title);

        // Başlıkta 'laptop' kelimesinin olup olmadığını doğrula
        Assert.assertTrue(title.toLowerCase().contains("laptop"), "Sayfa başlığında 'laptop' kelimesi bulunamadı.");
    }

    @Test
    public void debugSearchTest() throws InterruptedException {
        driver.get("https://www.amazon.com.tr");

        // Beklemek için doğru süreyi ayarlama
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // 5 saniye bekle
        wait.until(ExpectedConditions.titleContains("Amazon")); // Amazon başlığının yüklenmesini bekle

        System.out.println("Page Title: " + driver.getTitle());
    }

    @AfterTest
    public void tearDown() {
        // Test tamamlandığında tarayıcıyı kapat
        if (driver != null) {
            driver.quit();
        }
    }
}
