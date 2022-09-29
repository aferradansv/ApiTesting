package com.testing.apiTesting.petStoreApi;

import com.aventstack.extentreports.Status;
import com.github.javafaker.Faker;
import com.testing.apiTesting.utils.World;
import com.testing.apiTesting.utils.ExtentTestManager;
import com.testing.apiTesting.pojos.addPet.AddPetRequest;
import com.testing.apiTesting.pojos.addPet.PetCategory;
import com.testing.apiTesting.pojos.addPet.PetTags;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Locale;

import static io.restassured.RestAssured.given;

@Component
public class ReusableMethodsPetStore {

    @Autowired
    private World world;

    @Autowired
    private ExtentTestManager extentTestManager;

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

        world.setResponse(given().spec(getSpecRequest())
                .accept(ContentType.JSON).contentType(ContentType.JSON).body(request)
                .when().post("/pet")
                .then().log().all().extract().response());

        extentTestManager.addApiCallsToReport(Status.PASS);

        Assert.assertEquals(world.getResponse().statusCode(), 200);
        extentTestManager.getTest().log(Status.PASS, "The Status Code returned is " + world.getResponse().statusCode() + "as expected");

        JsonPath jp = new JsonPath(world.getResponse().body().asString());
        String id = jp.getString("id");
        extentTestManager.getTest().log(Status.INFO, "The petID is : " + id);
        return id;
    }


    public RequestSpecification getSpecRequest(){
        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.log().all().header("api_key", key)
                .filter(new RequestLoggingFilter(world.getRequestCapture()))
                .filter(new ResponseLoggingFilter(world.getResponseCapture()))
                .baseUri(baseURL);

        return requestSpec;
    }

}
