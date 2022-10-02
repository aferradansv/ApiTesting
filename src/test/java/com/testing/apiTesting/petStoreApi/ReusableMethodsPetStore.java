package com.testing.apiTesting.petStoreApi;

import com.aventstack.extentreports.Status;
import com.testing.apiTesting.pojos.pets.Pet;
import com.testing.apiTesting.utils.ExtentTestManager;
import com.testing.apiTesting.utils.LogApiToExtentReports;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class ReusableMethodsPetStore {

    @Autowired
    private ExtentTestManager testManager;

    @Value("${petStore.base.url}")
    private String baseURL;

    @Value("${petStore.api.key}")
    private String key;


    public Pet addPet() {
        Pet response = given().spec(getSpecRequest())
                .accept(ContentType.JSON).contentType(ContentType.JSON).body(new Pet())
                .when().post("/pet")
                .then().log().all().statusCode(200)
                .extract().response().as(Pet.class);

        testManager.getTest().log(Status.INFO, "The petID is : " + response.getId());
        return response;
    }


    public RequestSpecification getSpecRequest(){
        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.log().all().header("api_key", key)
                .baseUri(baseURL)
                .filter(new LogApiToExtentReports());
        return requestSpec;
    }

}
