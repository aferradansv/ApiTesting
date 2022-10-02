package com.testing.apiTesting.utils;

import com.aventstack.extentreports.Status;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.testing.apiTesting.SpringContext;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.SneakyThrows;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.logging.XMLFormatter;

import static com.google.gson.JsonParser.parseString;

public class LogApiToExtentReports implements Filter {

    private ExtentTestManager testManager;

    private void getExtentTestManager() {
        testManager = SpringContext.getBean(ExtentTestManager.class);
    }

    @SneakyThrows
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        getExtentTestManager();
        Response response = ctx.next(requestSpec, responseSpec);

        String requestString = "Request method: " + requestSpec.getMethod() + System.lineSeparator() +
                "Request Path: " + requestSpec.getURI() + System.lineSeparator() +
                "Request Params: " + getParamsString(requestSpec.getRequestParams()) + System.lineSeparator() +
                "Query Params: " + getParamsString(requestSpec.getQueryParams()) + System.lineSeparator() +
                "Form Params: " + getParamsString(requestSpec.getFormParams()) + System.lineSeparator() +
                "Path Params: " + getParamsString(requestSpec.getPathParams()) + System.lineSeparator() +
                "Headers: " + getHeaderString(requestSpec.getHeaders()) + System.lineSeparator() +
                "Body: " + System.lineSeparator() + getBodyString(requestSpec.getBody());

        String responseString = response.getStatusLine() + System.lineSeparator()
                + getHeaderStringNoLeftSpaces(response.getHeaders()) + System.lineSeparator()
                + getBodyString(response.body().prettyPrint()) + System.lineSeparator();

        testManager.addApiCallsToReport(Status.INFO, requestString, responseString, response.time());

        return response;
    }

    private String getParamsString(Object reqParams) {
//        @SuppressWarnings("unchecked")
        Map<String, String> params = (Map<String, String>) reqParams;
        StringBuilder returnString = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                returnString.append(System.lineSeparator()).append("  ").append(entry.getKey()).append(" = ").append(entry.getValue()).append(System.lineSeparator());
                first = false;
            } else {
                returnString.append("  ").append(entry.getKey()).append(" = ").append(entry.getValue()).append(System.lineSeparator());
            }
        }
        return returnString.toString();
    }

    private String getBodyString(Object body) throws IOException, DocumentException {
        if (body != null) {
            String bodyToString = body.toString().trim();
            if (bodyToString.startsWith("{")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement je = parseString(body.toString().trim());
                return gson.toJson(je);
            } else if (bodyToString.startsWith("<")) {
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setIndentSize(2);
                format.setSuppressDeclaration(true);
                format.setEncoding("UTF-8");

                org.dom4j.Document document = DocumentHelper.parseText(bodyToString);
                StringWriter sw = new StringWriter();
                XMLWriter writer = new XMLWriter(sw, format);
                writer.write(document);
                return sw.toString();
            } else {
                return bodyToString;
            }
        }
        return null;
    }

    private String getHeaderString(Headers headers) {
        StringBuilder returnString = new StringBuilder();
        boolean first = true;
        for (Header h : headers) {
            if (first) {
                returnString.append(System.lineSeparator()).append("  ").append(h.getName()).append(" = ").append(h.getValue()).append(System.lineSeparator());
                first = false;
            } else {
                returnString.append("  ").append(h.getName()).append(" = ").append(h.getValue()).append(System.lineSeparator());
            }
        }
        return returnString.toString();
    }

    private String getHeaderStringNoLeftSpaces(Headers headers) {
        StringBuilder returnString = new StringBuilder();
        for (Header h : headers) {
            returnString.append(h.getName()).append(" = ").append(h.getValue()).append(System.lineSeparator());
        }
        return returnString.toString();
    }

}
