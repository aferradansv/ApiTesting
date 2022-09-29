package com.testing.apiTesting.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExtentTestManager {

    @Autowired
    private World world;

    private ExtentTest test;

    private final ExtentReports extentReports;

    public ExtentTestManager() {
        extentReports = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("src/test/java/test-output/TestReport.html");
        spark.config(
                ExtentSparkReporterConfig.builder()
                        .theme(Theme.DARK)
                        .documentTitle("API Automation Testing")
                        .build()
        );
        extentReports.attachReporter(spark);
    }

    public void startTest(String testName, String description) {
        test = extentReports.createTest(testName, description);
    }

    public ExtentTest getTest() {
        return test;
    }

    public void endTest() {
        extentReports.flush();
    }

    public void addApiCallsToReport(Status status) {
        getTest().log(status, reportTableResultForAPICalls());
        world.getRequestWriter().getBuffer().setLength(0);
        world.getResponseWriter().getBuffer().setLength(0);
    }

    private String reportTableResultForAPICalls() {
        String[][] data = new String[2][3];
        data[0][0] = "Request Details";
        data[0][1] = "Response Details";
        data[0][2] = "Time (ms)";
        data[1][0] = getMarkup(world.getRequestWriter().toString());
        data[1][1] = getMarkup(world.getResponseWriter().toString());
        data[1][2] = String.valueOf(getResponseTime());
        return getMarkUpForTable(data);
    }

    private long getResponseTime() {
        if (world.getResponse() != null)
            return world.getResponse().time();
        else
            return 0;
    }

    private String getMarkup(String code) {
        String lhs = "<textarea disabled class='code-block' rows='20' cols='100' style='height:100%;background-color: #222;color:#bbb !important;'>";
        String rhs = "</textarea>";
        return lhs + code + rhs;
    }

    private String getMarkUpForTable(String[][] data) {
        StringBuilder sb = new StringBuilder();
        //sb.append("<table height='10' width='10'>");
        sb.append("<table style='height:auto;width:auto'>");
        for (String[] datum : data) {
            sb.append("<tr>");
            for (String s : datum) {
                sb.append("<td style='text-align:left'>").append(s).append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }


}
