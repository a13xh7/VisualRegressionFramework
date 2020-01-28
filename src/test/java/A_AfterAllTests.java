import org.testng.annotations.Test;
import framework.DriverContainer;
import framework.Report;

public class A_AfterAllTests
{
    @Test
    public void generateReport() {
        Report.generate();

        DriverContainer.getCurrentDriver().quit();
        DriverContainer.removeDriver();
    }
}
