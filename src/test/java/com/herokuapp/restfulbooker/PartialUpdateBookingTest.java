package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.config.HttpClientConfig.httpClientConfig;

public class PartialUpdateBookingTest extends BaseTest{

    private static final Logger logger = LogManager.getLogger(PartialUpdateBookingTest.class);

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
    public void partialUpdateBooking() {
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

            // Actualizar la reserva
            JSONObject body = new JSONObject();
            body.put("firstname", "Dante");

            JSONObject bookingdates = new JSONObject();
            bookingdates.put("checkin", "2020-04-25");
            bookingdates.put("checkout", "2020-04-27");

            body.put("bookingdates", bookingdates);

            // Enviar la solicitud PATCH para actualizar parcialmente la reserva
            Response responseUpdate = RestAssured.given()
                    .auth().preemptive().basic("admin", "password123")
                    .contentType(ContentType.JSON)
                    .body(body.toString())
                    .patch("/" + bookingid);

            // Imprimir la respuesta de la actualización
            logger.info("Update Booking Response: " + responseUpdate.asString());

            // Validar el código de estado de la actualización
            softAssert.assertEquals(responseUpdate.getStatusCode(), 200, "Status Code should be 200, but it's not");

            // Validar los campos actualizados
            softAssert.assertEquals(responseUpdate.jsonPath().getString("firstname"), "Dante", "firstname in response is not expected");
             softAssert.assertEquals(responseUpdate.jsonPath().getString("bookingdates.checkin"), "2020-04-25", "checkin in response is not expected");
            softAssert.assertEquals(responseUpdate.jsonPath().getString("bookingdates.checkout"), "2020-04-27", "checkout in response is not expected");

            softAssert.assertAll();

        } catch (Exception e) {
            logger.error("An exception occurred: ", e);
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }


}
