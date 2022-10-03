package com.testing.apiTesting.petStoreApi.users;

import com.aventstack.extentreports.Status;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
import com.testing.apiTesting.pojos.ApiMessage;
import com.testing.apiTesting.pojos.user.User;
import com.testing.apiTesting.utils.ExtentTestManager;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(CustomListeners.class)
@SpringBootTest(classes = ApiTestingApplication.class)
public class CreateUsersTestCase extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager testManager;

    @Test(testName = "Create a new User",
            description = "Create a new user in the Store")
    public void CreateNewUserInStore() {
        methods.addUser();
    }

    @Test(testName = "Create a new User with empty body",
            description = "Create a new user with no values passed in the body fails and returns a 0 as an userId")
    public void AddNewUserEmptyBody() {
        ApiMessage response = given().spec(methods.getSpecRequest()).body("{}")
                .accept(ContentType.JSON).contentType(ContentType.JSON)
                .when().post("/user")
                .then().statusCode(200).extract().response().as(ApiMessage.class);

        assertThat(response.getMessage()).isEqualTo("0");
        testManager.getTest().log(Status.PASS, "The user id returned is: " + response.getMessage() + ". That means that user has not been created");
    }

    @Test(testName = "Create new Users With Array",
            description = "Create a new set of users in the Store been passed as an Array")
    public void CreateNewUsersInStoreArray() {
        User[] listOfUsers = new User[3];
        listOfUsers[0] = new User();
        listOfUsers[1] = new User();
        listOfUsers[2] = new User();

        ApiMessage response = given().spec(methods.getSpecRequest()).body(listOfUsers)
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when().post("/user/createWithList")
                .then().statusCode(200).extract().response().as(ApiMessage.class);

        assertThat(response.getMessage()).isEqualTo("ok");
        testManager.getTest().log(Status.PASS, "The users have been created");
    }

    @Test(testName = "Create new Users With List",
            description = "Create a new set of users in the Store been passed as an List")
    public void CreateNewUsersInStoreList() {
        ArrayList<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(new User());
        listOfUsers.add(new User());
        listOfUsers.add(new User());

        ApiMessage response = given().spec(methods.getSpecRequest()).body(listOfUsers)
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when().post("/user/createWithList")
                .then().statusCode(200).extract().response().as(ApiMessage.class);

        assertThat(response.getMessage()).isEqualTo("ok");
        testManager.getTest().log(Status.PASS, "The users have been created");
    }

}
