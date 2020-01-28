package app;

import framework.Driver;
import helpers.Trim;
import framework.DriverContainer;

public class App {

    public App() {
        // for example

        // IndexPage indexPage = PageBuilder.buildIndexPage();

        // and use in like this

        // app.index.open()
    }

    public void open() {
        DriverContainer.getCurrentDriver().get(AppConfig.baseUrl);
    }

    public void openPage(String pageUrl) {
        String url = Trim.rtrim(AppConfig.baseUrl, "/") + "/" + Trim.ltrim(pageUrl, "/");
        DriverContainer.getCurrentDriver().get(url);
    }

    /*
    PREPARE page for screenshot
    For example hide header or footer
     */
    public void preparePageForScreenshot()
    {
        scrollBottomTop();
        scrollBottomTop();
        Driver.wait(1);
        scrollBottomTop();
        scrollBottomTop();
        Driver.wait(1);
    }

    public void scrollBottomTop()
    {
        Driver.executeJs("window.scrollTo(0,document.body.scrollHeight);");
        Driver.executeJs("window.scrollTo(0,0);");
    }
}
