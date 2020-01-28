package framework;

import app.AppConfig;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Driver {

    public static WebDriver currentDriver() {
        return DriverContainer.getCurrentDriver();
    }

    public static void open(String url) {
        currentDriver().get(url);
    }

    public static void clearCookies() {
        open(AppConfig.baseUrl);
        currentDriver().manage().deleteAllCookies();
        executeJs("localStorage.clear();");
    }

    public static void quit() {
        currentDriver().quit();
        DriverContainer.removeDriver();
    }

    public static void refresh() {
        currentDriver().navigate().refresh();
    }

    public static void executeJs(String script) {
        JavascriptExecutor js = (JavascriptExecutor)currentDriver();
        try {
            js.executeScript(script);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void waitForUrlContains(String urlChunk) {
        WebDriverWait wait = new WebDriverWait(currentDriver(), 10);
        wait.until(ExpectedConditions.urlContains(urlChunk));
    }

    public static void waitForUrlDoesNotContain(String urlChunk) {
        int maxTime = 20;
        while(  currentDriver().getCurrentUrl().contains(urlChunk)  && maxTime > 0) {
            wait(1);
            maxTime--;
        }
    }

    public static void changeWindowSize(int width, int height) {
        currentDriver().manage().window().setSize(new Dimension(width, height));
    }

    public static void maximize() {
        currentDriver().manage().window().maximize();
    }

    public static void wait(int seconds)
    {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
