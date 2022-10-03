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

@Listeners(CustomListeners.class)
@SpringBootTest(classes = ApiTestingApplication.class)
public class DeleteUserTestCase extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager testManager;

    @Test(testName = "Delete an existing User",
            description = "Delete an existing user in the Store")
    public void DeleteUserInStore() {
        User user = methods.addUser();
        given().spec(methods.getSpecRequest()).pathParam("username", user.getUsername())
                .when().delete("/user/{username}")
                .then().statusCode(200);

        testManager.getTest().log(Status.PASS, "User has been deleted successfully");
    }

    @Test(testName = "Delete a non existing User",
            description = "Delete a non existing user in the Store")
    public void DeleteNonExistingUserInStore() {
        String username = "iDoNotExist";
        given().spec(methods.getSpecRequest()).pathParam("username", username)
                .when().delete("/user/{username}")
                .then().statusCode(404);

        testManager.getTest().log(Status.PASS, "User does not exist so nothing is deleted");
    }

}
