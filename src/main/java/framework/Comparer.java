package framework;

import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.Screenshot;

public class Comparer {

    /******************************************************************************************
     * Compare PAGE screenshots with all breakpoints
     ******************************************************************************************/

    public static void comparePages(String testName)
    {
        for (String breakpoint : TestConfig.breakpoints())
        {
            Driver.changeWindowSize(Integer.parseInt(breakpoint), 1000);
            Screenshoter.makePageScreenshot( testName + TestConfig.breakpointDelimiter + breakpoint);
            compareImages( testName + TestConfig.breakpointDelimiter  + breakpoint);
        }
    }

    public static void comparePages(String testName, String[] ignoredElementsCssSelectors)
    {
        for (String breakpoint : TestConfig.breakpoints())
        {
            Driver.changeWindowSize(Integer.parseInt(breakpoint), 1000);

            for (String cssSelector: ignoredElementsCssSelectors) {
                Driver.executeJs(String.format("document.querySelector('%s').style.visibility = 'hidden';", cssSelector));
            }

            Screenshoter.makePageScreenshot( testName + TestConfig.breakpointDelimiter + breakpoint);
            compareImages( testName + TestConfig.breakpointDelimiter  + breakpoint);
        }
    }

    /******************************************************************************************
     * Compare PAGE screenshots with specified breakpoint
     ******************************************************************************************/

    public static void comparePagesWithBreakpoint(String testName, String breakpoint)
    {
        Driver.changeWindowSize(Integer.parseInt(breakpoint), 1000);
        Screenshoter.makePageScreenshot( testName + TestConfig.breakpointDelimiter + breakpoint);
        compareImages( testName + TestConfig.breakpointDelimiter  + breakpoint);
    }

    public static void comparePagesWithBreakpoint(String testName, String breakpoint, String[] ignoredElementsCssSelectors)
    {
        Driver.changeWindowSize(Integer.parseInt(breakpoint), 1000);

        for (String cssSelector: ignoredElementsCssSelectors) {
            Driver.executeJs(String.format("document.querySelector('%s').style.visibility = 'hidden';", cssSelector));
        }

        Screenshoter.makePageScreenshot( testName + TestConfig.breakpointDelimiter + breakpoint);
        compareImages( testName + TestConfig.breakpointDelimiter  + breakpoint);
    }


    /******************************************************************************************
     * Compare ELEMENT screenshots with all breakpoints
     ******************************************************************************************/

    public static void compareElements(String testName, String elementCssLocator)
    {
        for (String breakpoint : TestConfig.breakpoints())
        {
            Driver.changeWindowSize(Integer.parseInt(breakpoint), 1000);
            Screenshoter.makeElementScreenshot( testName + TestConfig.breakpointDelimiter + breakpoint, elementCssLocator);
            compareImages( testName + TestConfig.breakpointDelimiter  + breakpoint);
        }
    }

    public static void compareElements(String testName, WebElement element)
    {
        for (String breakpoint : TestConfig.breakpoints())
        {
            Driver.changeWindowSize(Integer.parseInt(breakpoint), 1000);
            Screenshoter.makeElementScreenshot( testName + TestConfig.breakpointDelimiter + breakpoint, element);
            compareImages( testName + TestConfig.breakpointDelimiter  + breakpoint);
        }
    }

    /******************************************************************************************
     * Compare ELEMENT screenshots with specified breakpoint
     ******************************************************************************************/

    public static void compareElementsWithBreakpoint(String testName, String breakpoint, String elementCssLocator)
    {
        Driver.changeWindowSize(Integer.parseInt(breakpoint), 1000);
        Screenshoter.makeElementScreenshot( testName + TestConfig.breakpointDelimiter + breakpoint, elementCssLocator);
        compareImages( testName + TestConfig.breakpointDelimiter  + breakpoint);
    }

    public static void compareElementsWithBreakpoint(String testName, String breakpoint, WebElement element)
    {
        Driver.changeWindowSize(Integer.parseInt(breakpoint), 1000);
        Screenshoter.makeElementScreenshot( testName + TestConfig.breakpointDelimiter + breakpoint, element);
        compareImages( testName + TestConfig.breakpointDelimiter  + breakpoint);
    }

    /******************************************************************************************
     * Compare images method
     ******************************************************************************************/

    private static void compareImages(String fileName)
    {
        Screenshot expected = Screenshoter.getExpectedScreenshot(fileName);
        Screenshot actual = Screenshoter.getActualScreenshot(fileName);

        int diffSize = Differ.getDiffSize(expected, actual);

        if(diffSize > TestConfig.allowableDiffSize)
        {
            Differ.saveDiffImage(expected, actual, fileName);
            Giffer.createGif(fileName);
        }
    }
}
