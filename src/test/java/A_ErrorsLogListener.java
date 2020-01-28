import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import framework.TestConfig;
import framework.Report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class A_ErrorsLogListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult TestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult TestResult) {

    }

    @Override
    public void onTestFailure(ITestResult TestResult)
    {
        Report.brokenTests++;

        String signature = buildSignatureForTestResult(TestResult);
        String stackTrace = buildStackTraceForTestResult(TestResult);

        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File(TestConfig.pathToErrorsLog()),true));
            writer.println("<hr> <pre>");
            writer.println("Failed test: " + signature);
            writer.println(TestResult.getThrowable().getMessage());
            writer.println("</pre>");
            writer.close();
        } catch (Exception e) {
            System.out.println("ERROR WHILE WRITING FAILED TESTS LOGS");
        }
    }

    @Override
    public void onTestSkipped(ITestResult TestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult TestResult) {

    }

    @Override
    public void onStart(ITestContext TestContext) {

    }

    @Override
    public void onFinish(ITestContext TestContext) {

    }

    private String buildStackTraceForTestResult(ITestResult iTestResult)
    {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] elements = iTestResult.getThrowable().getStackTrace();

        for (StackTraceElement element : elements) {
            if (!element.getClassName().matches("(sun.reflect|java.lang.reflect|org.testng).*")) {
                sb.append("    at ").append(element.toString()).append("\n");
            }
        }

        return sb.toString();
    }

    private String buildSignatureForTestResult(ITestResult iTestResult)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(iTestResult.getTestClass().getName());
        sb.append("#");
        sb.append(iTestResult.getMethod().getMethodName());
        sb.append("(");

        Object[] parameters = iTestResult.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            sb.append(parameter.toString());
            if (i < parameters.length) {
                sb.append(", ");
            }
        }
        sb.append(")");

        return sb.toString();
    }
}
