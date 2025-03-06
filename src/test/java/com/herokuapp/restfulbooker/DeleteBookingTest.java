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

public class DeleteBookingTest extends BaseTest{

    private static final Logger logger = LogManager.getLogger(DeleteBookingTest.class);

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
    public void deleteBooking() {
        try {
            // Crear una nueva reserva
            Response response = createNewBooking();
            logger.info("Create Booking Response: " + response.asString());
            SoftAssert softAssert = new SoftAssert();

            // Validar que la creación de la reserva fue exitosa
            softAssert.assertEquals(response.getStatusCode(), 200, "Booking creation failed");

            // Obtener el ID de la reserva
            Integer bookingid = response.jsonPath().getInt("bookingid");
            if (bookingid == null || bookingid == 0) {
                logger.error("Booking ID not found in the response");
                Assert.fail("Booking ID not found in the response");
            }


            Response responseDelete = RestAssured.given()
                    .auth().preemptive().basic("admin", "password123")
                    .delete("/" + bookingid);

            // Imprimir la respuesta de la actualización
            logger.info("Delete Booking Response: " + responseDelete.asString());

            // Validar el código de estado de la actualización
            softAssert.assertEquals(responseDelete.getStatusCode(), 201, "Status Code should be 201, but it's not");

            Response responseGet = RestAssured.get("/" + bookingid);
            logger.info("Delete Booking Response: " + responseGet.asString());
            Assert.assertEquals(responseGet.getBody().asString(), "Not Found", "Body should be 'Not Found', but it's not.");

        } catch (Exception e) {
            logger.error("An exception occurred: ", e);
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }


}
