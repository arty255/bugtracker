package integration;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class WebDriverConfig {
    protected static Properties properties;

    static {
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/selenium.properties")) {
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void init() {
        System.setProperty("webdriver.chrome.driver", WebDriverConfig.getProperty("webdriver.chrome.driver"));
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    public static WebDriver getWebDriverWithOptions() {
        WebDriver webDriver = new ChromeDriver(getChromeOptions());
        webDriver.manage().window().maximize();
        return webDriver;
    }
}