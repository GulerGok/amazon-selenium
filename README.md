✅ README.md
markdown
Kopyala
Düzenle
# Amazon Selenium Test Projesi

Bu proje, Java + Selenium + TestNG kullanılarak Amazon Türkiye sitesinde basit bir arama testi gerçekleştirmek amacıyla hazırlanmıştır. Maven kullanılarak bağımlılıklar yönetilir.

---

## 🔧 Kullanılan Teknolojiler

- Java 11
- Selenium WebDriver 4.9.0
- WebDriverManager 4.4.0
- TestNG 7.4.0
- Maven

---

## 🧪 Test Senaryosu

Amazon ana sayfasına gidilir.  
Arama kutusuna `"laptop"` yazılır ve arama yapılır.  
Sayfa başlığında `"laptop"` kelimesinin geçtiği doğrulanır.

---

## 🚀 Kurulum ve Çalıştırma

### 1. Reposu klonlayın
```bash
git clone https://github.com/GulerGok/amazon-selenium.git
cd amazon-selenium
2. Maven bağımlılıklarını indir
bash
Kopyala
Düzenle
mvn clean install
3. Testleri çalıştır
bash
Kopyala
Düzenle
mvn test
📄 testng.xml Dosyası (isteğe bağlı)
Eğer testleri TestNG suite üzerinden çalıştırmak istiyorsan kök dizine aşağıdaki gibi bir testng.xml dosyası ekleyebilirsin:

xml
Kopyala
Düzenle
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Amazon Test Suite">
    <test name="Amazon Search Test">
        <classes>
            <class name="tests.AmazonTest"/>
        </classes>
    </test>
</suite>
📝 Kod Örneği
java
Kopyala
Düzenle
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
🤝 Katkı
Pull request'ler ve issue'lar her zaman memnuniyetle karşılanır. İyileştirme önerilerin varsa paylaşmaktan çekinme 😊

📄 Lisans
Bu proje MIT Lisansı ile lisanslanmıştır. 