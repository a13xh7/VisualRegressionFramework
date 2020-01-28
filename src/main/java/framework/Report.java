package framework;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Report
{
    public static int brokenTests = 0;

    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    private static String menuLinkTemplate = "<a href='#' class='w3-bar-item w3-button w3-left-align w3-leftbar @@LINK_CLASS@@ test_link' onclick=\"openLink(this, '@@TEST_ID@@')\">@@TEST_NAME@@</a>";

    private static List<String> uniqueTestNames = new ArrayList<>();

    public static void generate() {

        String htmlTemplate = null;

        try {
            htmlTemplate = new String(Files.readAllBytes(Paths.get(TestConfig.pathToReportTemplate())), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        htmlTemplate = htmlTemplate.replace("@@SIDEBAR_MENU@@", generateSidebarMenu());
        htmlTemplate = htmlTemplate.replace("@@TESTS@@", generateTestsContent());

        htmlTemplate = htmlTemplate.replace("@@ALL@@", Integer.toString(uniqueTestNames.size()) );
        htmlTemplate = htmlTemplate.replace("@@FAILED@@", Integer.toString(countFailedTests()) );
        htmlTemplate = htmlTemplate.replace("@@PASSED@@", Integer.toString(uniqueTestNames.size() - countFailedTests()) );

        htmlTemplate = htmlTemplate.replace("@@ERRORS_LOG@@", generateErrorsLog());

        htmlTemplate = htmlTemplate.replace("@@BROWSER@@", TestConfig.browser);
        htmlTemplate = htmlTemplate.replace("@@BROKEN_TESTS@@", Integer.toString(brokenTests));

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(TestConfig.pathToReportOutput(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        writer.println(htmlTemplate);
        writer.close();
    }

    private static int countFailedTests() {
        int failed = 0;
        for (String testName : uniqueTestNames)
        {
           if(isTestFailed(testName)) {
               failed++;
           }
        }
        return failed;
    }


    public static String generateSidebarMenu() {

        // 1 - Check all actual files and collect unique test names

        File directory = new File(TestConfig.pathToActual());
        File[] filesList = directory.listFiles();
        Arrays.sort(filesList);

        // 2 - Collect unique test names

        for (File file : filesList)
        {
            if (!file.isFile()) {
                continue;
            }

            String testName = FilenameUtils.removeExtension(file.getName());
            testName = testName.substring(0, testName.indexOf(TestConfig.breakpointDelimiter));

            if(!uniqueTestNames.contains(testName)) {
                uniqueTestNames.add(testName);
            }
        }

        // 3 - GENERATE SIDEBAR MENU HTML

        String generatedHtml = "";

        int id = 0;
        for (String testName : uniqueTestNames)
        {
            id++;
            String testLink = menuLinkTemplate;

            if(isTestFailed(testName)) {
                testLink = testLink.replace("@@LINK_CLASS@@", "w3-border-red");
            } else {
                testLink = testLink.replace("@@LINK_CLASS@@", "w3-border-green");
            }

            testLink = testLink.replace("@@TEST_ID@@", "test" + id);
            testLink = testLink.replace("@@TEST_NAME@@", testName);

            generatedHtml = generatedHtml + testLink + System.lineSeparator();
        }

        return generatedHtml;
    }

    private static String generateTestsContent() {

        String generatedHtml = "";

        int id = 0;
        for (String testName : uniqueTestNames)
        {
            id++;

            generatedHtml+= "<div class='w3-container w3-animate-right test_result' id='test" + id + "'>" + System.lineSeparator();
            generatedHtml+= "<h1 class='w3-center'>" + testName + "</h1>" + System.lineSeparator();
            generatedHtml+= "<hr>" + System.lineSeparator();
            generatedHtml+= "<div class='w3-bar w3-center'>" + System.lineSeparator();

            // GENERATE BREAKPOINTS BUTTONS

            String breakpointButtonsHtml = "";
            int breakpointId = 0;
            for (String breakpoint : TestConfig.breakpoints())
            {
                breakpointId++;
                int width = Integer.parseInt(breakpoint);
                String fileName = testName + TestConfig.breakpointDelimiter + breakpoint + ".png";
                String buttonClass = isBreakpointFailed(fileName) ? "w3-red" : "w3-white";

                if(fileExist(TestConfig.pathToActual() + fileName)) {
                    breakpointButtonsHtml+= "<button class='w3-button " + buttonClass + " w3-round w3-border' onclick='openBreakpoint(\"" + getBreakpointType(width) + id + breakpointId + "\")'>" + System.lineSeparator();
                    breakpointButtonsHtml+= "<img class='" + getBreakpointType(width) + "' width='64' height='64'>" + System.lineSeparator();
                    breakpointButtonsHtml+= "<b>" + width + " px</b>" + System.lineSeparator();
                    breakpointButtonsHtml+= "</button>" + System.lineSeparator();
                }
            }

            generatedHtml+= breakpointButtonsHtml;
            generatedHtml+= "</div>" + System.lineSeparator();

            // GENERATE BREAKPOINTS TABS FOR BUTTONS

            String breakpointTabsHtml = "";
            breakpointId = 0;
            for (String breakpoint : TestConfig.breakpoints())
            {
                breakpointId++;
                int width = Integer.parseInt(breakpoint);

                String fileName = testName + TestConfig.breakpointDelimiter + breakpoint + ".png";

                String breakpointBorderClass = "";
                String gridSize = "m3";

                if(isBreakpointFailed(fileName)) {
                    breakpointBorderClass = "w3-border-red";
                    gridSize = "m3";
                } else {
                    breakpointBorderClass = "w3-border-green";
                    gridSize = "m6";
                }

                if(fileExist(TestConfig.pathToActual() + fileName))
                {
                    breakpointTabsHtml+= "<div id='" + getBreakpointType(width) + id + breakpointId + "' class='test_breakpoint' style='display:none'>" + System.lineSeparator();
                    breakpointTabsHtml+= "<div class='w3-row w3-white w3-card w3-padding-16 w3-section w3-leftbar " + breakpointBorderClass + "' style='padding-right:5px;'>" + System.lineSeparator();
                    breakpointTabsHtml+= "<div class='w3-margin-bottom w3-margin-left'><b class='w3-text-gray'>File name:</b> <b>" + fileName +"</b></div>" + System.lineSeparator();

                    breakpointTabsHtml+= "<div class='w3-col "+gridSize+" w3-center'>" + System.lineSeparator();
                    breakpointTabsHtml+= "<b>EXPECTED</b>" + System.lineSeparator();
                    breakpointTabsHtml+= "<br>" + System.lineSeparator();
                    breakpointTabsHtml+= "<img class='test_img' src='../" +  TestConfig.pathToExpected() + testName + TestConfig.breakpointDelimiter + breakpoint + ".png" + "' alt='image'>" + System.lineSeparator();
                    breakpointTabsHtml+= "</div>" + System.lineSeparator();

                    breakpointTabsHtml+= "<div class='w3-col "+ gridSize +" w3-center'>" + System.lineSeparator();
                    breakpointTabsHtml+= "<b>ACTUAL</b>" + System.lineSeparator();
                    breakpointTabsHtml+= "<br>" + System.lineSeparator();
                    breakpointTabsHtml+= "<img class='test_img' src='../" + TestConfig.pathToActual() + testName + TestConfig.breakpointDelimiter + breakpoint + ".png" + "' alt='image'>" + System.lineSeparator();
                    breakpointTabsHtml+= "</div>" + System.lineSeparator();

                    if(isBreakpointFailed(fileName)) {
                        breakpointTabsHtml+= "<div class='w3-col m3 w3-center'>" + System.lineSeparator();
                        breakpointTabsHtml+= "<b>DIFFERENCE</b>" + System.lineSeparator();
                        breakpointTabsHtml+= "<br>" + System.lineSeparator();
                        breakpointTabsHtml+= "<img class='test_img' src='../" + TestConfig.pathToDiff() + testName + TestConfig.breakpointDelimiter + breakpoint + ".png" + "' alt='image'>" + System.lineSeparator();
                        breakpointTabsHtml+= "</div>" + System.lineSeparator();

                        breakpointTabsHtml+= "<div class='w3-col m3 w3-center'>" + System.lineSeparator();
                        breakpointTabsHtml+= "<b>GIF</b>" + System.lineSeparator();
                        breakpointTabsHtml+= "<br>" + System.lineSeparator();
                        breakpointTabsHtml+= "<img class='test_img' src='../" + TestConfig.pathToGif() + testName + TestConfig.breakpointDelimiter + breakpoint + ".gif" + "' alt='image'>" + System.lineSeparator();
                        breakpointTabsHtml+= "</div>" + System.lineSeparator();
                    }

                    breakpointTabsHtml+= "</div>" + System.lineSeparator();
                    breakpointTabsHtml+= "</div>" + System.lineSeparator();
                }
            }

            generatedHtml+= breakpointTabsHtml + "</div>";
        }

        return generatedHtml;
    }

    private static String getBreakpointType(int width) {
        String breakpointType = "desktop";
        if(width >= 1024) {
            breakpointType = "desktop";
        } else if(width >= 768 && width < 1024) {
            breakpointType = "tablet";
        } else if(width < 768) {
            breakpointType = "mobile";
        }
        return breakpointType;
    }

    private static boolean isTestFailed(String testName) {

        boolean isFailed = false;

        for (String breakpoint : TestConfig.breakpoints())
        {
            File f = new File(TestConfig.pathToDiff() + testName + TestConfig.breakpointDelimiter + breakpoint + ".png");

            if(f.exists() && !f.isDirectory()) {
                isFailed = true;
            }
        }
        return isFailed;
    }

    /**
     *
     * @param fileName string - full name - testName_breakpoint_fileExtension - test_1920.png
     * @return boolean
     */
    private static boolean isBreakpointFailed(String fileName)  {
        File f = new File(TestConfig.pathToDiff() + fileName);
        return f.exists() && !f.isDirectory();
    }

    private static boolean fileExist(String pathToFile) {
        File f = new File(pathToFile);
        return f.exists() && !f.isDirectory();
    }

    private static String generateErrorsLog()
    {
        String errorsLog = "";

        try {
            errorsLog = new String(Files.readAllBytes(Paths.get(TestConfig.pathToErrorsLog())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return errorsLog;
    }

}
