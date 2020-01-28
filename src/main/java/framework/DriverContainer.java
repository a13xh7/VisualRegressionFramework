package framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;

public class DriverContainer {

    private static HashMap<Integer, WebDriver> drivers = new HashMap<Integer, WebDriver>();

    public static WebDriver getCurrentDriver() {
        Integer threadId = (int)Thread.currentThread().getId();
        if(drivers.get(threadId) != null) {
            return drivers.get(threadId);
        } else {
            initDriver();
            return drivers.get(threadId);
        }
    }

    private static void initDriver() {

        switch (TestConfig.browser)
        {
            case "chrome":

                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                if(TestConfig.headless.contains("1")) {
                    chromeOptions.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors", "--silent", "--hide-scrollbars");
                }

                addDriver(new ChromeDriver(chromeOptions));
                break;

            case "firefox":

                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if(TestConfig.headless.contains("1")) {
                    firefoxOptions.setHeadless(true);
                }

                addDriver(new FirefoxDriver(firefoxOptions));
                break;

            default:

                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions2 = new ChromeOptions();

                if(TestConfig.headless.contains("1")) {
                    chromeOptions2.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors", "--silent", "--hide-scrollbars");
                }

                addDriver(new ChromeDriver(chromeOptions2));
                break;
        }

        getCurrentDriver().manage().window().maximize();
    }

    public static void addDriver(WebDriver driver) {
        Integer threadId = (int)Thread.currentThread().getId();
        drivers.put(threadId, driver);
    }

    public static void removeDriver() {
        Integer threadId = (int)Thread.currentThread().getId();
        drivers.remove(threadId);
    }

    public static void executeJs(String script) {
        JavascriptExecutor js = (JavascriptExecutor) DriverContainer.getCurrentDriver();
        try {
            js.executeScript(script);
        } catch (Exception e) {}
    }

}
