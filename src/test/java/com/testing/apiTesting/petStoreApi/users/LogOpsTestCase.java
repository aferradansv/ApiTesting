package com.testing.apiTesting.petStoreApi.users;

import com.aventstack.extentreports.Status;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
import com.testing.apiTesting.pojos.ApiMessage;
import com.testing.apiTesting.pojos.user.User;
import com.testing.apiTesting.utils.ExtentTestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(CustomListeners.class)
@SpringBootTest(classes = ApiTestingApplication.class)
public class LogOpsTestCase extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager testManager;

    @Test(testName = "Log In with correct credentials",
            description = "Log In with correct credentials")
    public void LogInStoreCorrectCredentials() {
        User user = methods.addUser();
        ApiMessage message = given().spec(methods.getSpecRequest()).queryParam("username", user.getUsername())
                .queryParam("password", user.getPassword())
                .when().get("/user/login")
                .then().statusCode(200).extract().body().as(ApiMessage.class);

        assertThat(message.getMessage()).contains("logged in user session:");
        String session = message.getMessage().split(":")[1];
        testManager.getTest().log(Status.PASS, "User has logged in successfully with session: " + session);
    }


}
