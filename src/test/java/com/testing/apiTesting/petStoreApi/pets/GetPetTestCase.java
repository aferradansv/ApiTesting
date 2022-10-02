package com.testing.apiTesting.petStoreApi.pets;

import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
import com.testing.apiTesting.pojos.pets.ApiMessage;
import com.testing.apiTesting.pojos.pets.Pet;
import com.testing.apiTesting.utils.ExtentTestManager;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
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
        Pet pet = methods.addPet();
        Pet response = given().spec(methods.getSpecRequest()).pathParam("petId", pet.getId())
                .when().get("/pet/{petId}")
                .then().statusCode(200).extract().response().as(Pet.class);

        assertEquals(pet.getId(), response.getId());
        testManager.getTest().log(Status.PASS, "The id returned in the get call: " + response.getId() + " is the same as the id when created " + pet.getId());
    }

    @Test(testName = "Get Pet by Id - Pet does not exist in the DB",
            description = "Retrieve the Pet details using an Id of a pet that doesn't exist")
    public void GetPetByIdDoesNotExist() {
        String petId = Faker.instance().number().digits(9);
        ApiMessage message = given().spec(methods.getSpecRequest())
                .pathParam("petId", petId)
                .when().get("/pet/{petId}")
                .then().log().all().statusCode(404)
                .extract().response().as(ApiMessage.class);

        assertEquals(message.getCode(), "1");
        assertEquals(message.getType(), "error");
        assertEquals(message.getMessage(), "Pet not found");
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

    @Test(testName = "Get Pet by Status - Available",
            description = "Retrieve the Pet details filtering by their statuses")
    public void getListPetsByStatusAvailable() {
        String status = "available";
        List<Pet> listOfPets = getListOfPetsByStatus(status);
        assertThat(listOfPets).isNotEmpty().extracting(Pet::getStatus)
                .contains("available").doesNotContain("sold").doesNotContain("pending");
        testManager.getTest().log(Status.PASS, "Only pets with status " + status + " have been displayed");
    }

    @Test(testName = "Get Pet by Status - Pending",
            description = "Retrieve the Pet details filtering by their statuses")
    public void getListPetsByStatusPending() {
        String status = "pending";
        List<Pet> listOfPets = getListOfPetsByStatus(status);
        assertThat(listOfPets).isNotEmpty().extracting(Pet::getStatus)
                .contains("pending").doesNotContain("sold").doesNotContain("available");
        testManager.getTest().log(Status.PASS, "Only pets with status " + status + " have been displayed");
    }

    @Test(testName = "Get Pet by Status - Sold",
            description = "Retrieve the Pet details filtering by their statuses")
    public void getListPetsByStatusSold() {
        String status = "sold";
        List<Pet> listOfPets = getListOfPetsByStatus(status);
        assertThat(listOfPets).isNotEmpty().extracting(Pet::getStatus)
                .contains("sold").doesNotContain("available").doesNotContain("pending");
        testManager.getTest().log(Status.PASS, "Only pets with status " + status + " have been displayed");
    }

    @Test(testName = "Get Pet by Status - Available, Sold",
            description = "Retrieve the Pet details filtering by their statuses")
    public void getListPetsByStatusAvailableSold() {
        String status = "available,sold";
        List<Pet> listOfPets = getListOfPetsByStatus(status);
        assertThat(listOfPets).isNotEmpty().extracting(Pet::getStatus)
                .contains("available").contains("sold").doesNotContain("pending");
        testManager.getTest().log(Status.PASS, "Only pets with status " + status + " have been displayed");
    }


    @Test(testName = "Get Pet by Status - Pending, Sold",
            description = "Retrieve the Pet details filtering by their statuses")
    public void getListPetsByStatusPendingSold() {
        String status = "pending,sold";
        List<Pet> listOfPets = getListOfPetsByStatus(status);
        assertThat(listOfPets).isNotEmpty().extracting(Pet::getStatus)
                .contains("pending").contains("sold").doesNotContain("available");
        testManager.getTest().log(Status.PASS, "Only pets with status " + status + " have been displayed");
    }


    @Test(testName = "Get Pet by Status - Available, Pending",
            description = "Retrieve the Pet details filtering by their statuses")
    public void getListPetsByStatusAvailablePending() {
        String status = "available,pending";
        List<Pet> listOfPets = getListOfPetsByStatus(status);
        assertThat(listOfPets).isNotEmpty().extracting(Pet::getStatus)
                .contains("pending").contains("available").doesNotContain("sold");
        testManager.getTest().log(Status.PASS, "Only pets with status " + status + " have been displayed");
    }

    @Test(testName = "Get Pet by Status - Wrong Status",
            description = "Retrieve the Pet details filtering by their statuses")
    public void getListPetsByStatusWrongStatus() {
        String status = "wrongStatus";
        List<Pet> listOfPets = getListOfPetsByStatus(status);
        assertThat(listOfPets).isEmpty();
        testManager.getTest().log(Status.PASS, "No pets with " + status + " have been recovered");
    }


    private List<Pet> getListOfPetsByStatus(String status) {
        return given().spec(methods.getSpecRequest())
                .queryParam("status", status)
                .accept(ContentType.JSON)
                .when().get("pet/findByStatus")
                .then().statusCode(200).extract().body().jsonPath().getList(".", Pet.class);
    }


}
