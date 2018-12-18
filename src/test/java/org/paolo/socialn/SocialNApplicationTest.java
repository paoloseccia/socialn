package org.paolo.socialn;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.paolo.socialn.persistance.model.Message;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SocialNApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SocialNApplicationTest {


    @LocalServerPort
    private int _port;


    @Test
    public void whenCreateNewMessage_thenCreated () {

        final String apiRoot = "http://localhost:" + _port + "/wall/paolo";

        final String createdAt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(System.currentTimeMillis());

        Response response = RestAssured.given().contentType(MediaType.TEXT_PLAIN_VALUE)
                .body("This is my fist message")
                .post(apiRoot);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());

        final Message actualMessage = response.getBody().as(Message.class);

        assertEquals("paolo", actualMessage.getUser().getUserName());
        assertEquals("This is my fist message", actualMessage.getText());
        assertEquals(createdAt, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(new DateTime(actualMessage.getDateCreated().getTime())));
    }


    @Test
    public void whenInvalidMessageLength_thenError () {

        final String apiRoot = "http://localhost:" + _port + "/wall/paolo";

        Response response = RestAssured.given().contentType(MediaType.TEXT_PLAIN_VALUE)
                .body(String.format("%141d", 0))
                .post(apiRoot);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }


    @Test
    public void whenFollowNewUser_thenUpdated () {

        RestAssured.given().contentType(MediaType.TEXT_PLAIN_VALUE)
                .body("This is my fist message").post("http://localhost:" + _port + "/wall/paolo");

        RestAssured.given().contentType(MediaType.TEXT_PLAIN_VALUE)
                .body("This is my fist message").post("http://localhost:" + _port + "/wall/marco");


        Response response = RestAssured.given().contentType(MediaType.TEXT_PLAIN_VALUE)
                .body("This is my fist message")
                .put("http://localhost:" + _port + "/wall/paolo/follow/marco");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        List actualUsers =  response.getBody().as(List.class);
        assertEquals(1, actualUsers.size());

    }

    @Test
    public void whenFollowMyself_thenError () {

        RestAssured.given().contentType(MediaType.TEXT_PLAIN_VALUE)
                .body("This is my fist message").post("http://localhost:" + _port + "/wall/paolo");


        Response response = RestAssured.given().contentType(MediaType.TEXT_PLAIN_VALUE)
                .body("This is my fist message")
                .put("http://localhost:" + _port + "/wall/paolo/follow/paolo");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());

    }


    @Test
    public void whenFollowUnknownUser_thenError () {

        RestAssured.given().contentType(MediaType.TEXT_PLAIN_VALUE)
                .body("This is my fist message").post("http://localhost:" + _port + "/wall/paolo");


        Response response = RestAssured.given().contentType(MediaType.TEXT_PLAIN_VALUE)
                .body("This is my fist message")
                .put("http://localhost:" + _port + "/wall/paolo/follow/marco");

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

}