package framework;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Screenshoter
{
    private static Screenshot actualScreenshot;
    private static Screenshot expectedScreenshot;

    private static File actualFile;
    private static File expectedFile;


    public static void makePageScreenshot(String fileName)
    {
        // make page screenshot

        actualScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(DriverContainer.getCurrentDriver());
        actualFile = new File(TestConfig.pathToActual() + fileName + ".png");

        // save page screenshot
        try {
            ImageIO.write(actualScreenshot.getImage(), "png", actualFile);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        saveActualScreenshotAsExpectedIfExpectedDoesNotExist(fileName);
    }

    public static void makeElementScreenshot(String fileName, String elementCssLocator)
    {
        // make element screenshot
        WebElement myWebElement = DriverContainer.getCurrentDriver().findElement(By.cssSelector(elementCssLocator));

        actualScreenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(200))
                .coordsProvider(new WebDriverCoordsProvider())
                .takeScreenshot(DriverContainer.getCurrentDriver(), myWebElement);

        actualFile = new File(TestConfig.pathToActual() + fileName + ".png");

        // save element screenshot
        try {
            ImageIO.write(actualScreenshot.getImage(), "png", actualFile);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        saveActualScreenshotAsExpectedIfExpectedDoesNotExist(fileName);
    }

    public static void makeElementScreenshot(String fileName, WebElement element)
    {
        // make element screenshot
        actualScreenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .coordsProvider(new WebDriverCoordsProvider())
                .takeScreenshot(DriverContainer.getCurrentDriver(), element);

        actualFile = new File(TestConfig.pathToActual() + fileName + ".png");

        // save element screenshot
        try {
            ImageIO.write(actualScreenshot.getImage(), "png", actualFile);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        saveActualScreenshotAsExpectedIfExpectedDoesNotExist(fileName);
    }

    private static void saveActualScreenshotAsExpectedIfExpectedDoesNotExist(String fileName)
    {
        expectedFile = new File(TestConfig.pathToExpected() + fileName + ".png");

        if(!expectedFile.exists()) {
            expectedScreenshot = actualScreenshot;
            try {
                ImageIO.write(actualScreenshot.getImage(), "png", expectedFile);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                expectedScreenshot = new Screenshot(ImageIO.read(expectedFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Screenshot getActualScreenshot(String fileName)
    {
        return actualScreenshot;
    }

    public static Screenshot getExpectedScreenshot(String fileName)
    {
        return expectedScreenshot;
    }

    // REMOVE SCREENSHOTS

    public static void removeExpectedScreenshots()
    {
        File directory = new File(TestConfig.pathToExpected());
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeActualScreenshots()
    {
        File directory = new File(TestConfig.pathToActual());
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeDiffScreenshots()
    {
        File directory = new File(TestConfig.pathToDiff());
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeGifScreenshots()
    {
        File directory = new File(TestConfig.pathToGif());
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createScreenshotsFolders() {
        try {
            Files.createDirectories(Paths.get(TestConfig.pathToExpected()));
            Files.createDirectories(Paths.get(TestConfig.pathToActual()));
            Files.createDirectories(Paths.get(TestConfig.pathToDiff()));
            Files.createDirectories(Paths.get(TestConfig.pathToGif()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
