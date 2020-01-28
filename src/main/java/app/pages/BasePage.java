package app.pages;

import app.AppConfig;
import helpers.Trim;
import framework.DriverContainer;

public abstract class BasePage {

    protected String pageUrl;

    public BasePage(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public void open() {
        String url = Trim.rtrim(AppConfig.baseUrl, "/") + "/" + Trim.ltrim(pageUrl, "/");
        DriverContainer.getCurrentDriver().get(url);
    }
}
