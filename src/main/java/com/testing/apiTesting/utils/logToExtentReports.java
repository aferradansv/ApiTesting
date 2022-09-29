//package com.testing.ApiTesting.Utils;
//
//import com.aventstack.extentreports.Status;
//import com.testing.ApiTesting.SpringContext;
//import io.restassured.filter.Filter;
//import io.restassured.filter.FilterContext;
//import io.restassured.response.Response;
//import io.restassured.specification.FilterableRequestSpecification;
//import io.restassured.specification.FilterableResponseSpecification;
//
//public class logToExtentReports implements Filter  {
//
//    private ExtentTestManager extentTestManager;
//
//    private void getExtentTestManager() {
//        extentTestManager = SpringContext.getBean(ExtentTestManager.class);
//    }
//
//    @Override
//    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
//        getExtentTestManager();
//        Response response = ctx.next(requestSpec, responseSpec);
//        String request = requestSpec.request().toString();
//        extentTestManager.addApiCallsToReport(Status.INFO, request , response.prettyPrint(), response.time());
//        return response;
//    }
//}
