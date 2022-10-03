package com.testing.apiTesting.petStoreApi.pets;

import com.aventstack.extentreports.Status;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
import com.testing.apiTesting.pojos.ApiMessage;
import com.testing.apiTesting.pojos.pets.Pet;
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
public class DeletePetsTestCase extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager testManager;

    @Test(testName = "Delete Pet by Id",
            description = "Delete an existing pet using an Id")
    public void DeletePetById() {
        Pet pet = methods.addPet();
        ApiMessage response = given().spec(methods.getSpecRequest()).pathParam("petId", pet.getId())
                .when().delete("/pet/{petId}")
                .then().statusCode(200).extract().response().as(ApiMessage.class);

        assertThat(response.getMessage()).isEqualTo(pet.getId());
        testManager.getTest().log(Status.PASS, "The pet with id " + pet.getId() + " has been removed");
    }

    @Test(testName = "Delete Pet by Id - Pet does not exist",
            description = "Delete an pet using an Id that does not exist in the database")
    public void DeletePetByIdNotExist() {
        String petId = "iDoNotExist";
        given().spec(methods.getSpecRequest()).pathParam("petId", petId)
                .when().delete("/pet/{petId}")
                .then().statusCode(404);

        testManager.getTest().log(Status.PASS, "The pet with id " + petId + " has been removed");
    }


}
