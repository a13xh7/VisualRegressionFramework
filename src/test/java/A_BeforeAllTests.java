import org.testng.annotations.Test;
import framework.TestConfig;
import framework.DriverContainer;
import framework.Screenshoter;

import java.io.PrintWriter;

public class A_BeforeAllTests
{
    @Test
    public void init() {

        // 0 - init config

        TestConfig.initConfig();

        // create browser folders if they don't exist

        Screenshoter.createScreenshotsFolders();

        // 1 - Remove temp screenshots

        Screenshoter.removeActualScreenshots();
        Screenshoter.removeDiffScreenshots();
        Screenshoter.removeGifScreenshots();

        // 2 - clear errors log

        clearErrorsLog();

        // 3 - Remove expected screenshots if needed

        if(TestConfig.clean.equals("1")) {
            Screenshoter.removeExpectedScreenshots();
        }

        // 4 - init web driver

        DriverContainer.getCurrentDriver();
    }

    private void clearErrorsLog()
    {
        try {
            PrintWriter writer = new PrintWriter(TestConfig.pathToErrorsLog(), "UTF-8");
            writer.println("");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
