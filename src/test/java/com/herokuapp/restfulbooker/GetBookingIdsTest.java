package com.herokuapp.restfulbooker;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

public class GetBookingIdsTest {

    @Test
    public void getBookingIdsWhithoutFilterTest(){

        Response response = RestAssured.get("https://restful-booker.herokuapp.com/booking");
        response.print();

        Assert.assertEquals(response.getStatusCode(),200, "Status Code should be 200, but it's not");

        List<Integer> bookingIds = response.jsonPath().getList("bookingid");
        Assert.assertFalse(bookingIds.isEmpty(), "List shouldn't be empty");

    }


}
