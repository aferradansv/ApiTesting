package com.testing.apiTesting.petStoreApi.pets;

import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
import com.testing.apiTesting.utils.World;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.utils.ExtentTestManager;
import io.restassured.path.json.JsonPath;
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
    private World world;

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager extentTestManager;

    @Test(testName = "Get Pet by Id",
            description = "Retrieve the Pet details using an existing Id")
    public void GetPetById() {
        String petId = methods.addPet();
        world.setResponse(given().spec(methods.getSpecRequest())
                .pathParam("petId", petId)
                .when().get("/pet/{petId}")
                .then().log().all().extract().response());

        extentTestManager.addApiCallsToReport(Status.PASS);

        Assert.assertEquals(world.getResponse().statusCode(), 200);
        extentTestManager.getTest().log(Status.PASS, "The Status Code returned is " + world.getResponse().statusCode() + " as expected");

        JsonPath jp = new JsonPath(world.getResponse().body().print());
        String petIdUpdated = jp.getString("id");
        assertEquals(petId, petIdUpdated);
        extentTestManager.getTest().log(Status.PASS, "The id returned in the get call: " + petIdUpdated + " is the same as the id when created " + petId);
    }

    @Test(testName = "Get Pet by Id - Pet does not exist in the DB",
            description = "Retrieve the Pet details using an Id of a pet that doesn't exist")
    public void GetPetByIdDoesNotExist() {
        String petId = Faker.instance().number().digits(9);
        world.setResponse(given().spec(methods.getSpecRequest())
                .pathParam("petId", petId)
                .when().get("/pet/{petId}")
                .then().log().all().extract().response());

        extentTestManager.addApiCallsToReport(Status.PASS);

        Assert.assertEquals(world.getResponse().statusCode(), 404);
        extentTestManager.getTest().log(Status.PASS, "The Status Code returned is " + world.getResponse().statusCode() + " as expected");

        JsonPath jp = new JsonPath(world.getResponse().body().asString());
        String errorMessage = jp.getString("message");
        Assert.assertEquals(errorMessage, "Pet not found");
        extentTestManager.getTest().log(Status.PASS, "The error message \"" + errorMessage + "\" is returned as expected");
    }

    @Test(testName = "Get Pet by Id - Id is not sent",
            description = "Retrieve the Pet details without sending any Id in the URL")
    public void GetPetByIdMissingId() {
        String petId = "";
        world.setResponse(given().spec(methods.getSpecRequest())
                .pathParam("petId", petId)
                .when().get("/pet/{petId}")
                .then().log().all().extract().response());

        extentTestManager.addApiCallsToReport(Status.PASS);

        Assert.assertEquals(world.getResponse().statusCode(), 405);
        extentTestManager.getTest().log(Status.PASS, "The Status Code returned is " + world.getResponse().statusCode() + " as expected");
    }

}
