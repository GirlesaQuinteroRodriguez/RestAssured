package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static io.restassured.config.HttpClientConfig.httpClientConfig;


public class GetBookingTest extends BaseTest{

    private static final Logger logger = LogManager.getLogger(GetBookingTest.class);

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        RestAssured.basePath = "/booking";

        RestAssured.config = RestAssuredConfig.config()
                .httpClient(httpClientConfig()
                        .setParam("http.connection.timeout", 5000) // Timeout de conexión
                        .setParam("http.socket.timeout", 5000)    // Timeout de socket
                );
    }

    @Test
    public void getBookingIdsWithoutFilterTest() {
        // Get response with booking ids
        Response response = RestAssured.given(spec).get("/booking");
        logger.info("Response" + response);

        // Verify response 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");

        // Verify at least 1 booking id in response
        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingIds.isEmpty(), "List of bookingIds is empty, but it shouldn't be");
    }

    @Test
    public void getBookingIdsWithFilterTest() {
        // adding query parameter to spec
        spec.queryParam("firstname", "Jim");
        spec.queryParam("lastname", "Smith");


        Response response = RestAssured.given(spec).get("/booking");
        logger.info("Response" + response);


        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200, but it's not");

        // Verify at least 1 booking id in response
        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingIds.isEmpty(), "List of bookingIds is empty, but it shouldn't be");
    }


    @Test
    public void getBookingTest() {
        try  {
            Response response = RestAssured.get("/1");
            logger.info("Response: " + response.asString());
            SoftAssert softAssert = new SoftAssert();

            Assert.assertEquals(response.getStatusCode(), 200, "Status Code should be 200, but it's not");
            Assert.assertNotNull(response.getBody(), "Response body should not be null");


            String firstName = response.jsonPath().getString("firstname");
            String lastName = response.jsonPath().getString("lastname");
            int price = response.jsonPath().getInt("totalprice");
            boolean deposit = response.jsonPath().getBoolean("depositpaid");
            String checkin = response.jsonPath().getString("bookingdates.checkin");

            Assert.assertNotNull(firstName, "First name should not be null");
            Assert.assertNotNull(lastName, "Last name should not be null");
            softAssert.assertEquals(firstName, "Mary", "Name is not the expected");
            softAssert.assertEquals(lastName, "Brown", "Lastname is not the expected");
            softAssert.assertFalse(deposit,"Deposit should be true, but it is not");
            softAssert.assertEquals(checkin, "2023-07-22", "checkin date is not correct");

            softAssert.assertAll();



        } catch (Exception e) {
            logger.error("An exception occurred: ", e);
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }
}