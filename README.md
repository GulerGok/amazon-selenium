# 🛒 Amazon Selenium Test Projesi

Bu proje, **Java**, **Selenium** ve **TestNG** kullanılarak Amazon Türkiye sitesinde basit bir arama testi gerçekleştirmek amacıyla hazırlanmıştır. **Maven** ile bağımlılık yönetimi sağlanır.

---

## 🔧 Kullanılan Teknolojiler

- ☕ Java 11
- 🧪 Selenium WebDriver 4.9.0
- ⚙️ WebDriverManager 4.4.0
- ✅ TestNG 7.4.0
- 📦 Maven

---

## 🧪 Test Senaryosu

1. Amazon ana sayfasına gidilir.
2. Arama kutusuna `"laptop"` yazılır ve arama yapılır.
3. Sayfa başlığında `"laptop"` kelimesinin geçtiği doğrulanır.

---

## 🚀 Kurulum ve Çalıştırma

### 1. Reposu Klonlayın
```bash
git clone https://github.com/GulerGok/amazon-selenium.git
cd amazon-selenium
```

2. Maven Bağımlılıklarını İndirin
```bash
mvn clean install
```

3. Testleri Çalıştırın
```bash
mvn test
```

🧾 testng.xml (İsteğe Bağlı)
Testleri TestNG suite üzerinden çalıştırmak için kök dizine aşağıdaki gibi bir testng.xml dosyası ekleyebilirsiniz:

```bash
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Amazon Test Suite">
    <test name="Amazon Search Test">
        <classes>
            <class name="tests.AmazonTest"/>
        </classes>
    </test>
</suite>
```

💻 Kod Örneği
```bash
public class AmazonTest {
WebDriver driver;

    @BeforeTest
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void searchTest() {
        driver.get("https://www.amazon.com.tr");
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys("laptop");
        searchBox.submit();

        String title = driver.getTitle();
        Assert.assertTrue(title.toLowerCase().contains("laptop"));
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
```

📄 Lisans
Bu proje MIT Lisansı ile lisanslanmıştır.