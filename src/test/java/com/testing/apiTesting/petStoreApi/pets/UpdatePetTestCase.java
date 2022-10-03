package com.testing.apiTesting.petStoreApi.pets;

import com.github.javafaker.Faker;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
import com.testing.apiTesting.pojos.ApiMessage;
import com.testing.apiTesting.pojos.pets.Pet;
import com.testing.apiTesting.utils.ExtentTestManager;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(CustomListeners.class)
@SpringBootTest(classes = ApiTestingApplication.class)
public class UpdatePetTestCase extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager testManager;

    @Test(testName = "Update pet details - Name and Status",
            description = "Update name and status of an existing pet")
    public void UpdatePet() {
        Pet pet = methods.addPet();
        String nameOriginal = pet.getName();
        String statusOriginal = pet.getStatus();

        Faker faker = new Faker(new Locale("en-GB"));
        pet.setName(faker.dog().name());
        pet.setStatus("sold");

        Pet petUpdated = given().spec(methods.getSpecRequest()).body(pet)
                .accept(ContentType.JSON).contentType(ContentType.JSON)
                .when().put("/pet")
                .then().statusCode(200).extract().response().as(Pet.class);

        assertThat(petUpdated.getName()).isNotEqualTo(nameOriginal);
        assertThat(petUpdated.getStatus()).isNotEqualTo(statusOriginal);
    }


    @Test(testName = "Upload new image for an existing pet",
            description = "Upload an image to an existing pet")
    public void UploadImage() {
        Pet pet = methods.addPet();
        String additionalMetadata = "Photo of a nice puppy";
        String fileName = "puppy.jpg";
        File puppyPhoto = new File("./src/main/java/com/testing/apiTesting/images/" + fileName);

        ApiMessage response = given().spec(methods.getSpecRequest()).pathParam("id", pet.getId())
                .contentType("multipart/form-data")
                .multiPart("file", puppyPhoto)
                .formParam("additionalMetadata", additionalMetadata)
                .when().post("/pet/{id}/uploadImage")
                .then().statusCode(200).extract().response().as(ApiMessage.class);

        assertThat(response.getMessage()).contains("additionalMetadata").contains(additionalMetadata)
                .contains(fileName).contains(String.valueOf(puppyPhoto.length()));
    }

}
