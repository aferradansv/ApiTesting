package com.testing.apiTesting.listeners;

import com.aventstack.extentreports.Status;
import com.testing.apiTesting.SpringContext;
import com.testing.apiTesting.utils.ExtentTestManager;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class CustomListeners implements ITestListener {

    private ExtentTestManager testManager;

    private void getExtentTestManager() {
        testManager = SpringContext.getBean(ExtentTestManager.class);
    }

    @Override
    public void onTestStart(ITestResult result) {
        getExtentTestManager();
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        Test test = method.getAnnotation(Test.class);
        String testName;
        if (result.getParameters().length != 0)
            testName = test.testName() + " - " + result.getParameters()[0];
        else
            testName = test.testName();
        testManager.startTest(testName, test.description());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testManager.getTest().log(Status.PASS, result.getName() + " PASS");
        testManager.endTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testManager.getTest().log(Status.FAIL, result.getName() + " Error --> " + result.getThrowable());
        testManager.endTest();
    }

}
