package com.testing.apiTesting.petStoreApi.users;

import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
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

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(CustomListeners.class)
@SpringBootTest(classes = ApiTestingApplication.class)
public class UpdateUserTestCase extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager testManager;


    @Test(testName = "Update an existing User",
            description = "Update an existing user in the Store")
    public void UpdateUserInStore() {
        User user = methods.addUser();
        String emailOriginal = user.getEmail();
        String firstNameOriginal = user.getFirstName();
        String lastNameOriginal = user.getLastName();

        Faker faker = new Faker(new Locale("en-GB"));
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());

        User userUpdated = given().spec(methods.getSpecRequest()).body(user).pathParam("username", user.getUsername())
                .accept(ContentType.JSON).contentType(ContentType.JSON)
                .when().put("/user/{username}")
                .then().statusCode(200).extract().as(User.class);

        assertThat(userUpdated.getEmail()).isNotEqualTo(emailOriginal);
        assertThat(userUpdated.getFirstName()).isNotEqualTo(firstNameOriginal);
        assertThat(userUpdated.getLastName()).isNotEqualTo(lastNameOriginal);

        testManager.getTest().log(Status.PASS, "The user has been successfully updated");
    }

    @Test(testName = "Update a non existing User",
            description = "Update a non existing user in the Store")
    public void CreateNewUserInStore() {
        String username = "iDoNotExit";
        ApiMessage message = given().spec(methods.getSpecRequest()).body("{}").pathParam("username", username)
                .accept(ContentType.JSON).contentType(ContentType.JSON)
                .when().put("/user/{username}")
                .then().statusCode(200).extract().as(ApiMessage.class);

        assertThat(message.getMessage()).isEqualTo("0");
        testManager.getTest().log(Status.PASS, "The user has not been successfully updated because it does not exist");
    }

}
