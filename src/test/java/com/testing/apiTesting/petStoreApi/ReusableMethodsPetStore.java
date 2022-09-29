package com.testing.apiTesting.petStoreApi;

import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
import com.testing.apiTesting.pojos.pets.AddPetRequest;
import com.testing.apiTesting.pojos.pets.PetCategory;
import com.testing.apiTesting.pojos.pets.PetResponse;
import com.testing.apiTesting.pojos.pets.PetTags;
import com.testing.apiTesting.utils.ExtentTestManager;
import com.testing.apiTesting.utils.LogApiToExtentReports;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Locale;

import static io.restassured.RestAssured.given;

@Component
public class ReusableMethodsPetStore {

    @Autowired
    private ExtentTestManager testManager;

    @Value("${petStore.base.url}")
    private String baseURL;

    @Value("${petStore.api.key}")
    private String key;


    public String addPet() {
        Faker faker = new Faker(new Locale("en-GB"));
        AddPetRequest request = new AddPetRequest();
        request.setCategory(new PetCategory(faker.number().randomDigit(), "dog"));
        request.setName(faker.dog().name());
        request.setPhotoUrls(new ArrayList<>() {{
            add("http://puppy_dog_pictures.jpg");
        }});
        request.setTags(new ArrayList<>() {{
            add(new PetTags(faker.number().randomDigit(), "doggies"));
        }});
        request.setStatus("available");

        PetResponse response = given().spec(getSpecRequest())
                .accept(ContentType.JSON).contentType(ContentType.JSON).body(request)
                .when().post("/pet")
                .then().log().all().statusCode(200)
                .extract().response().as(PetResponse.class);

        testManager.getTest().log(Status.INFO, "The petID is : " + response.getId());
        return response.getId();
    }


    public RequestSpecification getSpecRequest(){
        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.log().all().header("api_key", key)
                .baseUri(baseURL)
                .filter(new LogApiToExtentReports());
        return requestSpec;
    }

}
