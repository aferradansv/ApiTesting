package com.testing.apiTesting.petStoreApi.store;

import com.aventstack.extentreports.Status;
import com.testing.apiTesting.ApiTestingApplication;
import com.testing.apiTesting.listeners.CustomListeners;
import com.testing.apiTesting.petStoreApi.ReusableMethodsPetStore;
import com.testing.apiTesting.pojos.ApiMessage;
import com.testing.apiTesting.pojos.store.Order;
import com.testing.apiTesting.utils.ExtentTestManager;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;

@Listeners(CustomListeners.class)
@SpringBootTest(classes = ApiTestingApplication.class)
public class StoreTestCase extends AbstractTestNGSpringContextTests {

    @Autowired
    private ReusableMethodsPetStore methods;

    @Autowired
    private ExtentTestManager testManager;

    @Test(testName = "Place a new Order in Store",
            description = "Place a new Order in Store")
    public void PlaceNewOrderInStore() {
        methods.addOrder();
    }

    @Test(testName = "Place an empty Order in Store",
            description = "Place an empty Order in Store")
    public void PlaceEmptyOrderInStore() {
        Order response = given().spec(methods.getSpecRequest()).body("{}")
                .accept(ContentType.JSON).contentType(ContentType.JSON)
                .when().post("/store/order")
                .then().statusCode(200).extract().response().as(Order.class);

        testManager.getTest().log(Status.PASS, "The order id returned is: " + response.getId());
    }

    @Test(testName = "Get an existing order",
            description = "Get an existing order in Store")
    public void GetExistingOrderInStore() {
        Order order = methods.addOrder();
        Order response = given().spec(methods.getSpecRequest()).pathParam("id", order.getId())
                .when().get("/store/order/{id}")
                .then().statusCode(200).extract().response().as(Order.class);

        assertThat(order).usingRecursiveComparison().isEqualTo(response);
        testManager.getTest().log(Status.PASS, "The order id returned by the get call is: " + order.getId());
    }

    @Test(testName = "Get an Non existing order",
            description = "Get an non existing order in Store")
    public void GetNonExistingOrderInStore() {
        String orderId = "1231646789654165374312";
        given().spec(methods.getSpecRequest()).pathParam("id", orderId)
                .when().get("/store/order/{id}")
                .then().statusCode(404);

        testManager.getTest().log(Status.PASS, "The order id " + orderId + " is not returned because it does not exist");
    }

    @Test(testName = "Get the store Inventory by Status",
            description = "Get the store Inventory filtered by the pet statuses")
    public void GetStoreInventory() {
        given().spec(methods.getSpecRequest())
                .when().get("/store/inventory")
                .then().statusCode(200);

        testManager.getTest().log(Status.PASS, "The inventory is returned");
    }

    @Test(testName = "Delete order by Id",
            description = "Delete an order with an existing Id")
    public void DeleteOrderById() {
        Order order = methods.addOrder();
        ApiMessage response = given().spec(methods.getSpecRequest()).pathParam("id", order.getId())
                .when().delete("/store/order/{id}")
                .then().statusCode(200).extract().response().as(ApiMessage.class);
        assertEquals(response.getMessage(), String.valueOf(order.getId()));
        testManager.getTest().log(Status.PASS, "The order" + order.getId() + " has been returned");
    }

    @Test(testName = "Delete order by Id - Non Existing Id",
            description = "Delete an order with a non existing Id")
    public void DeleteOrderByNonExistingId() {
        String orderId = "1231646789654165374312";
        given().spec(methods.getSpecRequest()).pathParam("id", orderId)
                .when().delete("/store/order/{id}")
                .then().statusCode(404);
        testManager.getTest().log(Status.PASS, "The order" + orderId + " does not exist");
    }

}
