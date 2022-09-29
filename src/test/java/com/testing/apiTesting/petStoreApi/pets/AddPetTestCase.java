package com.testing.apiTesting.petStoreApi.pets;

import com.aventstack.extentreports.Status;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
import com.testing.apiTesting.pojos.pets.PetResponse;
import com.testing.apiTesting.utils.ExtentTestManager;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(CustomListeners.class)
@SpringBootTest(classes = ApiTestingApplication.class)
public class AddPetTestCase extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager testManager;

    @Test(testName = "Add a Pet successfully",
            description = "Send correct Data to the API ")
    public void AddPetSuccessfully() {
        methods.addPet();
    }

    @Test(testName = "Add a Pet successfully with all fields missing",
            description = "Check that NO fields are mandatory in the request")
    public void AddPetFieldsInRequestNotMandatory() {
       PetResponse response = given().spec(methods.getSpecRequest())
                .accept(ContentType.JSON).contentType(ContentType.JSON).body("{}")
                .when().post("/pet")
                .then().statusCode(200)
               .extract().response().as(PetResponse.class);

        testManager.getTest().log(Status.INFO, "The petID is : " + response.getId());
    }
}
