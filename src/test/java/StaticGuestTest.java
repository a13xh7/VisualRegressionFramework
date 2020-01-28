import org.testng.annotations.Test;
import framework.Comparer;

public class StaticGuestTest extends A_BaseTest {

    @Test
    public void indexPage()
    {
        app.open();
        app.preparePageForScreenshot();
        Comparer.comparePages("index_page");
    }
}
