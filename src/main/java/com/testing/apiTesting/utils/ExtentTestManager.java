package com.testing.apiTesting.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.springframework.stereotype.Component;

@Component
public class ExtentTestManager {

    private ExtentTest test;

    private final ExtentReports extentReports;

    public ExtentTestManager() {
        extentReports = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("src/test-output/TestReport.html");
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

    public void addApiCallsToReport(Status status, String request, String response, long time) {
        getTest().log(status, reportTableResultForAPICalls(request, response, time));
    }

    private String reportTableResultForAPICalls(String request, String response, long time) {
        String[][] data = new String[2][3];
        data[0][0] = "Request Details";
        data[0][1] = "Response Details";
        data[0][2] = "Time (ms)";
        data[1][0] = getMarkup(request);
        data[1][1] = getMarkup(response);
        data[1][2] = String.valueOf(time);
        return getMarkUpForTable(data);
    }

    private String getMarkup(String code) {
        String lhs = "<textarea disabled class='code-block' rows='20' cols='100' style='height:100%;background-color: #222;color:#bbb !important;'>";
        String rhs = "</textarea>";
        return lhs + code + rhs;
    }

    private String getMarkUpForTable(String[][] data) {
        StringBuilder sb = new StringBuilder();
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
