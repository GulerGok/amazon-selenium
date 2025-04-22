package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.HomePage;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/Program Files/JetBrains/webdrivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Captcha kontrolü
        HomePage homePage = new HomePage(driver);
        if (homePage.isCaptchaPage()) {
            System.out.println("Captcha sayfası algılandı. Test durduruluyor.");
            driver.quit();  // tarayıcıyı kapatmayı unutma
            throw new SkipException("Captcha algılandı, test atlandı.");
        }

        driver.get("https://www.amazon.com");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
