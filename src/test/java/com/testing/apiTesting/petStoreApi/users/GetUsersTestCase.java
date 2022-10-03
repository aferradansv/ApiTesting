package com.testing.apiTesting.petStoreApi.users;

import com.aventstack.extentreports.Status;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
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
public class GetUsersTestCase extends AbstractTestNGSpringContextTests  {

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager testManager;

    @Test(testName = "Get an existing customer User",
            description = "Get an existing customer User")
    public void GetExistingUserInStore() {
        User userCreated = methods.addUser();
        User userGet = given().spec(methods.getSpecRequest()).pathParam("userId",userCreated.getUsername())
                .when().get("/user/{userId}")
                .then().statusCode(200).extract().response().as(User.class);

        assertThat(userGet).usingRecursiveComparison().isEqualTo(userCreated);
        testManager.getTest().log(Status.PASS, "The User has been successfully retrieved");
    }


    @Test(testName = "Get a Non existing customer User",
            description = "Get a non existing customer User")
    public void GetNonExistingUserInStore() {
        String userId = "iDoNotExist";
        given().spec(methods.getSpecRequest()).pathParam("userId",userId)
                .when().get("/user/{userId}")
                .then().statusCode(404);

        testManager.getTest().log(Status.PASS, "The user id: " + userId + " does not exist");
    }

}
