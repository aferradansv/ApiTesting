package com.testing.apiTesting.petStoreApi.pets;

import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
import com.testing.apiTesting.pojos.pets.ErrorMessage;
import com.testing.apiTesting.pojos.pets.PetResponse;
import com.testing.apiTesting.utils.ExtentTestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

@Listeners(CustomListeners.class)
@SpringBootTest(classes = ApiTestingApplication.class)
public class GetPetTestCase extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager testManager;

    @Test(testName = "Get Pet by Id",
            description = "Retrieve the Pet details using an existing Id")
    public void GetPetById() {
        String petId = methods.addPet();
        PetResponse response = given().spec(methods.getSpecRequest())
                .pathParam("petId", petId)
                .when().get("/pet/{petId}")
                .then().log().all().statusCode(200)
                .extract().response().as(PetResponse.class);

        assertEquals(petId, response.getId());
        testManager.getTest().log(Status.PASS, "The id returned in the get call: " + response.getId() + " is the same as the id when created " + petId);
    }

    @Test(testName = "Get Pet by Id - Pet does not exist in the DB",
            description = "Retrieve the Pet details using an Id of a pet that doesn't exist")
    public void GetPetByIdDoesNotExist() {
        String petId = Faker.instance().number().digits(9);
        ErrorMessage errorMessage = given().spec(methods.getSpecRequest())
                .pathParam("petId", petId)
                .when().get("/pet/{petId}")
                .then().log().all().statusCode(404)
                .extract().response().as(ErrorMessage.class);

        Assert.assertEquals(errorMessage.getCode(), "1");
        Assert.assertEquals(errorMessage.getType(), "error");
        Assert.assertEquals(errorMessage.getMessage(), "Pet not found");
        testManager.getTest().log(Status.PASS, "The error message is returned as expected");
    }

    @Test(testName = "Get Pet by Id - Id is not sent",
            description = "Retrieve the Pet details without sending any Id in the URL")
    public void GetPetByIdMissingId() {
        String petId = "";
        given().spec(methods.getSpecRequest())
                .pathParam("petId", petId)
                .when().get("/pet/{petId}")
                .then().log().all().statusCode(405);

    }

}
