package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import static io.restassured.config.HttpClientConfig.httpClientConfig;

public class CreateBookingTest extends BaseTest{

    private static final Logger logger = LogManager.getLogger(CreateBookingTest.class);

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        RestAssured.basePath = "/booking";

        RestAssured.config = RestAssuredConfig.config()
                .httpClient(httpClientConfig()
                        .setParam("http.connection.timeout", 5000) // Timeout de conexión
                        .setParam("http.socket.timeout", 7000)    // Timeout de socket
                );
    }

    @Test
    public void createBooking() {
        try {

            Response response = createNewBooking();
            logger.info("Response: " + response.asString());
            SoftAssert softAssert = new SoftAssert();

            Assert.assertEquals(response.getStatusCode(), 200, "Status Code should be 200, but it's not");
            String actualFirstName = response.jsonPath().getString("booking.firstname");
            softAssert.assertEquals(actualFirstName, "Girle", "firstname in response is not expected");

            String actualLastName = response.jsonPath().getString("booking.lastname");
            softAssert.assertEquals(actualLastName, "De Palacio", "lastname in response is not expected");

            int price = response.jsonPath().getInt("booking.totalprice");
            softAssert.assertEquals(price, 2000, "totalprice in response is not expected");

            boolean depositpaid = response.jsonPath().getBoolean("booking.depositpaid");
            softAssert.assertTrue(depositpaid, "depositpaid should be false, but it's not");

            String actualCheckin = response.jsonPath().getString("booking.bookingdates.checkin");
            softAssert.assertEquals(actualCheckin, "2025-03-03", "checkin in response is not expected");

            String actualCheckout = response.jsonPath().getString("booking.bookingdates.checkout");
            softAssert.assertEquals(actualCheckout, "2025-04-04", "checkout in response is not expected");

            String actualAdditionalneeds = response.jsonPath().getString("booking.additionalneeds");
            softAssert.assertEquals(actualAdditionalneeds, "jacuzzi", "additionalneeds in response is not expected");

            softAssert.assertAll();

        } catch (Exception e) {
            logger.error("An exception occurred: ", e);
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }


    @Test
    public void createBookingWithPOJOTest() {
        // Create body using POJOs
        BookingDates bookingdates = new BookingDates("2025-03-25", "2025-03-27");
        Booking booking = new Booking("SG", "PQ", 2000, false, bookingdates, "jacuzzi");

        // Get response
        Response response = RestAssured.given(spec)
                .contentType(ContentType.JSON)  // Especifica el tipo de contenido como JSON
                .body(booking)
                .post();

        logger.info("RESPONSE STATUS CODE: " + response.getStatusCode());
        logger.info("RESPONSE BODY: " + response.asString());

        if (response.getContentType().contains("application/json")) {
            BookingID bookingid = response.as(BookingID.class);

            // Log the request and response booking details
            logger.info("Request booking : " + booking.toString());
            logger.info("Response booking: " + bookingid.getBooking().toString());

            // Verify All fields
            Assert.assertEquals(bookingid.getBooking().toString(), booking.toString());
        } else {
            // Handle non-JSON responses (e.g., error messages)
            logger.error("Unexpected response content type: " + response.getContentType());
            logger.error("Response body: " + response.asString());
            Assert.fail("Unexpected response content type: " + response.getContentType());
        }

    }


}
