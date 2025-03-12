package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected RequestSpecification spec;

    @BeforeMethod
    public void setUp() {
        // Configura el spec con la URI base, el ContentType y el basePath
        spec = new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")
                .setBasePath("/booking")  // Configura el basePath
                .setContentType(ContentType.JSON)  // Configura el ContentType por defecto
                .build();
    }

    protected Response createNewBooking() {
        JSONObject body = new JSONObject();
        body.put("firstname", "Girle");
        body.put("lastname", "De Palacio");
        body.put("totalprice", 2000);
        body.put("depositpaid", true);

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2025-03-03");
        bookingDates.put("checkout", "2025-04-04");
        body.put("bookingdates", bookingDates);
        body.put("additionalneeds", "jacuzzi");

        Response response = RestAssured.given(spec)
                .body(body.toString())
                .post();
        return response;
    }

}
