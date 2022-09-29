package com.testing.apiTesting.utils;

import io.restassured.response.Response;
import lombok.Data;
import org.apache.commons.io.output.WriterOutputStream;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.io.StringWriter;

@Component
@Data
public class World {

    private StringWriter requestWriter;
    private PrintStream requestCapture;
    private StringWriter responseWriter;
    private PrintStream responseCapture;

    private Response response;

    public World() {
        requestWriter = new StringWriter();
        requestCapture = new PrintStream(new WriterOutputStream(requestWriter), true);
        responseWriter = new StringWriter();
        responseCapture = new PrintStream(new WriterOutputStream (responseWriter), true);
    }
}
