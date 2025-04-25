package com.mycompany.tests;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;

public class ScreenshotHelper {

    // Ekran görüntüsü almak için yardımcı metod
    public static void takeScreenshot(WebDriver driver, String fileName) {
        // Screenshot almak için driver'ı 'TakesScreenshot' tipine dönüştürüyoruz
        TakesScreenshot ts = (TakesScreenshot) driver;
        // Screenshot'u alıyoruz
        File source = ts.getScreenshotAs(OutputType.FILE);
        // Hedef dosya yolunu belirliyoruz (örneğin 'screenshots' klasörü altında)
        File destination = new File("screenshots/" + fileName + ".png");

        try {
            // Screenshot'u belirlenen hedefe kaydediyoruz
            FileUtils.copyFile(source, destination);
            System.out.println("Ekran görüntüsü alındı: " + destination.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Ekran görüntüsü alınırken hata oluştu: " + e.getMessage());
        }
    }
}
