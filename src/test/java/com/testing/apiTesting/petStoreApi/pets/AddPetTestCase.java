package com.testing.apiTesting.petStoreApi.pets;

import com.aventstack.extentreports.Status;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
import com.testing.apiTesting.utils.ExtentTestManager;
import com.testing.apiTesting.utils.World;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(CustomListeners.class)
@SpringBootTest(classes = ApiTestingApplication.class)
public class AddPetTestCase extends AbstractTestNGSpringContextTests {

    @Autowired
    private World world;

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager extentTestManager;

    @Value("${petStore.base.url}")
    private String baseURL;

    @Value("${petStore.api.key}")
    private String key;

    @Test(testName = "Add a Pet successfully",
            description = "Send correct Data to the API ")
    public void AddPetSuccessfully() {
        methods.addPet();
    }

    @Test(testName = "Add a Pet successfully with all fields missing",
            description = "Check that NO fields are mandatory in the request")
    public void AddPetFieldsInRequestNotMandatory() {
       world.setResponse(given().header("api_key", key).spec(methods.getSpecRequest())
                .baseUri(baseURL)
                .accept(ContentType.JSON).contentType(ContentType.JSON).body("{}")
                .when().post("/pet")
                .then().statusCode(200)
               .extract().response());

        extentTestManager.addApiCallsToReport(Status.PASS);

        Assert.assertEquals(world.getResponse().statusCode(), 200);
        extentTestManager.getTest().log(Status.PASS, "The Status Code returned is " + world.getResponse().statusCode() + "as expected");

        JsonPath jp = new JsonPath(world.getResponse().body().asString());
        String id = jp.getString("id");
        extentTestManager.getTest().log(Status.INFO, "The petID is : " + id);
    }
}
