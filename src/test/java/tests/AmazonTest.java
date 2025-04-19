package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class AmazonTest {

    WebDriver driver;

    @BeforeTest
    public void setUp() {
        // WebDriverManager ile ChromeDriver'ı otomatik olarak yükle
        WebDriverManager.chromedriver().setup();
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

    @AfterTest
    public void tearDown() {
        // Test tamamlandığında tarayıcıyı kapat
        if (driver != null) {
            driver.quit();
        }
    }
}
