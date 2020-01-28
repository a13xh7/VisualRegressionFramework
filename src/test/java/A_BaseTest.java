import app.App;
import framework.DriverContainer;
import org.testng.annotations.*;

public class A_BaseTest
{
    protected App app;

    @BeforeClass
    public void initBrowser()
    {
        DriverContainer.getCurrentDriver();
        app = new App();
    }

    @AfterMethod
    public void removeCookies()
    {
        try {
            DriverContainer.executeJs("window.localStorage.clear();");
            DriverContainer.getCurrentDriver().manage().deleteAllCookies();
        } catch (Exception e) {}
    }
}
