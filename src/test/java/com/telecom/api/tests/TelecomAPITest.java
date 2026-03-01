package com.telecom.api.tests;

import com.telecom.api.base.BaseTest;
import com.telecom.api.listeners.TestListener;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Listeners(TestListener.class)
public class TelecomAPITest extends BaseTest {
    @Test(priority = 1)
    public void addUser() {

        double randomNumber = Math.floor(Math.random()*10000);
        email = "testuser" + randomNumber + "@fake.com";

        String body = "{\n" +
                "\"firstName\": \"Test\",\n" +
                "\"lastName\": \"User\",\n" +
                "\"email\": \"" + email + "\",\n" +
                "\"password\": \"Password123\"\n" +
                "}";

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .post("/users");

        response.then().statusCode(201);

        token = response.jsonPath().getString("token");
        Assert.assertNotNull(token);
    }


    @Test(priority = 2)
    public void getUserProfile() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/users/me")
                .then()
                .statusCode(200);
    }

    @Test(priority = 3)
    public void updateUser() {

        String body = "{\n" +
                "\"firstName\": \"Updated\",\n" +
                "\"lastName\": \"User\"\n" +
                "}";

        given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .patch("/users/me")
                .then()
                .statusCode(200);
    }


    @Test(priority = 4)
    public void loginUser() {

        String body = "{\n" +
                "\"email\": \"" + email + "\",\n" +
                "\"password\": \"Password123\"\n" +
                "}";

        Response response =
                given()
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .post("/users/login");

        response.then().statusCode(200);

        token = response.jsonPath().getString("token");
        Assert.assertNotNull(token);
    }


    @Test(priority = 5)
    public void addContact() {

        String body = "{\n" +
                "\"firstName\": \"Pooja\",\n" +
                "\"lastName\": \"Thule\",\n" +
                "\"birthdate\": \"1996-10-17\",\n" +
                "\"email\": \""+email+"\",\n" +
                "\"phone\": \"8005555555\",\n" +
                "\"street1\": \"501\",\n" +
                "\"city\": \"Mumbai\",\n" +
                "\"stateProvince\": \"MH\",\n" +
                "\"postalCode\": \"400043\",\n" +
                "\"country\": \"India\"\n" +
                "}";

        Response response =
                given()
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .post("/contacts");

        response.then().statusCode(201);

        contactId = response.jsonPath().getString("_id");
        Assert.assertNotNull(contactId);
    }


    @Test(priority = 6)
    public void getContactList() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/contacts")
                .then()
                .statusCode(200);
    }

    @Test(priority = 7)
    public void getContact() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/contacts/" + contactId)
                .then()
                .statusCode(200);
    }


    @Test(priority = 8)
    public void updateContact() {

        String body = "{\n" +
                "\"firstName\": \"Pooja Pandurang\",\n" +
                "\"lastName\": \"Thule\",\n" +
                "\"birthdate\": \"1996-10-17\",\n" +
                "\"email\": \""+email+"\",\n" +
                "\"phone\": \"8005555555\",\n" +
                "\"street1\": \"501\",\n" +
                "\"city\": \"Mumbai\",\n" +
                "\"stateProvince\": \"MH\",\n" +
                "\"postalCode\": \"400043\",\n" +
                "\"country\": \"India\"\n" +
                "}";

        given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .put("/contacts/" + contactId)
                .then()
                .statusCode(200);
    }


    @Test(priority = 9)
    public void partialUpdate() {

        String body = "{ \"firstName\": \"Pooja P\" }";

        given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .patch("/contacts/" + contactId)
                .then()
                .statusCode(200);
    }


    @Test(priority = 10)
    public void logoutUser() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post("/users/logout")
                .then()
                .statusCode(200);
    }
}