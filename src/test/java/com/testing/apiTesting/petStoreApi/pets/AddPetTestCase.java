package com.testing.apiTesting.petStoreApi.pets;

import com.aventstack.extentreports.Status;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
import com.testing.apiTesting.pojos.pets.Pet;
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

    @Test(testName = "Add a Pet successfully with Json body",
            description = "Add a pet sending correct data in Json format")
    public void AddPetSuccessfullyWithJsonBody() {
        methods.addPet();
    }

    @Test(testName = "Add a Pet successfully with Xml body",
            description = "Add a pet sending correct data in Json format")
    public void AddPetSuccessfullyWithXmlBody() {
        Pet response = given().spec(methods.getSpecRequest())
                .accept(ContentType.XML).contentType(ContentType.XML).body(new Pet())
                .when().post("/pet")
                .then().log().all().statusCode(200)
                .extract().response().as(Pet.class);

        testManager.getTest().log(Status.INFO, "The petID is : " + response.getId());
    }

    @Test(testName = "Add a Pet successfully with all fields missing",
            description = "Check that NO fields are mandatory in the request")
    public void AddPetFieldsInRequestNotMandatory() {
        Pet response = given().spec(methods.getSpecRequest())
                .accept(ContentType.JSON).contentType(ContentType.JSON).body("{}")
                .when().post("/pet")
                .then().statusCode(200)
               .extract().response().as(Pet.class);

        testManager.getTest().log(Status.INFO, "The petID is : " + response.getId());
    }
}
