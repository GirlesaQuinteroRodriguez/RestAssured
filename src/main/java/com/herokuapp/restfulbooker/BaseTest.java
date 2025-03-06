package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

public class BaseTest {

    protected static Response createNewBooking() {
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

        Response response = (Response) RestAssured.given().contentType(ContentType.JSON).body(body.toString()).post();
        return response;
    }

}
