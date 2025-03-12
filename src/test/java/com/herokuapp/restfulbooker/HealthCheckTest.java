package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HealthCheckTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(UpdateBookingTest.class);

    @Test
    public void healthCheckTest(){
        given().
                spec(spec);
        when().
        get("/ping").
        then().assertThat().statusCode(201);
    }

    @Test
    public void headersAndCookiesTest() {
        Header someHeader = new Header("some_name", "Some_value");
        spec.header(someHeader);

        Cookie someCookie = new Cookie.Builder("some cookie", "some cookie value").build();
        spec.cookie(someCookie);


        Response response = RestAssured.given(spec).
                cookie("Test cookie name", "Test cookie value").
                header("Test header name", "Test header Value").log().all().get("/ping");

        // Get headers
        Headers headers = response.getHeaders();
        logger.info("Headers: " + headers.asList());

        Header serverHeader1 = headers.get("Server");
        logger.info(serverHeader1.getName() + ": " + serverHeader1.getValue());

        String serverHeader2 = response.getHeader("Server");
        logger.info("Server: " + serverHeader2.toString());

        // Get cookies
        Cookies cookies = response.getDetailedCookies();
        logger.info("Cookies: " + cookies.asList());

    }


}
