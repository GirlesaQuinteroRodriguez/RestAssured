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

import static io.restassured.config.HttpClientConfig.httpClientConfig;

public class CreateBookingTest {

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
    public void createBooking() {
        try  {
            Response response = RestAssured.get("/1");
            logger.info("Response: " + response.asString());
            SoftAssert softAssert = new SoftAssert();

            Assert.assertEquals(response.getStatusCode(), 200, "Status Code should be 200, but it's not");
            Assert.assertNotNull(response.getBody(), "Response body should not be null");



        } catch (Exception e) {
            logger.error("An exception occurred: ", e);
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }
}
