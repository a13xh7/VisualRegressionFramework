package framework;

import org.openqa.selenium.WebDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestConfig {

    public static String browser = "chrome";
    public static String clean = "0";
    public static String headless = "1";

    public static int allowableDiffSize;

    public static WebDriver driver;

    public static String breakpointDelimiter = "__";

    private static Properties properties;

    // STATIC INITIALIZER

    static {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/config/visual.properties");
            properties.load(fis);

            System.out.println();

        } catch (IOException e) {
            System.err.println("File not found");
        }

        allowableDiffSize = Integer.parseInt(properties.getProperty("allowableDiffSize"));
    }

    public static void initConfig()
    {
        browser = System.getProperty("browser") == null ? "chrome" : System.getProperty("browser");
        clean = System.getProperty("clean") == null ? "0" : System.getProperty("clean");
        headless = System.getProperty("headless") == null ? "1" : System.getProperty("headless");
    }

    public static void setWebDriver(WebDriver driver) {
        TestConfig.driver = driver;
    }

    // PATS TO SCREENSHOTS

    public static String pathToExpected() {
        return properties.getProperty("screenshots.expected") + TestConfig.browser + "/";
    }

    public static String pathToActual() {
        return properties.getProperty("screenshots.actual") + TestConfig.browser + "/";
    }

    public static String pathToDiff() {
        return properties.getProperty("screenshots.diff") + TestConfig.browser + "/";
    }

    public static String pathToGif() {
        return properties.getProperty("screenshots.gif") + TestConfig.browser + "/";
    }

    // REPORT TEMPLATE

    public static String pathToReportTemplate() {
        return properties.getProperty("report.template");
    }

    public static String pathToReportOutput() {
        return properties.getProperty("report.output");
    }

    public static String pathToErrorsLog() {
        return properties.getProperty("report.errors_log");
    }

    // BREAKPOINTS ARRAY
    public static String[] breakpoints() {
        return properties.getProperty("breakpoints").split(",");
    }

}
