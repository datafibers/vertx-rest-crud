package com.tymoshenko.controller.verticle;

import com.jayway.restassured.RestAssured;
import com.tymoshenko.model.Whisky;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

/**
 * This integration test should be run via Maven:
 * mvn clean verify
 *
 * This will simulate the "production" environment with deployed Verticle and running HttpServer.
 *
 * You can run this integration test from IDE as well provided you launched the fat jar before:
 * target/vertx-rest-crud-1.0-SNAPSHOT-fat.jar
 *
 * @author Yakiv
 * @since 15.03.2016
 */
public class WhiskyCrudRestServiceIT {

    public static final String BOURBON = "Bourbon";
    public static final String USA = "USA";
    public static final String NAME_BOURBON_ORIGIN_USA_JSON = "{\"name\":" + "\"" + BOURBON + "\", \"origin\":" + "\"" + USA + "\"}";

    @BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = Integer.getInteger("http.port", 8080);

        RestAssured.authentication = basic("tim", "sausages");
    }

    @AfterClass
    public static void unconfigureRestAssured() {
        RestAssured.reset();
    }

    @Test
    public void testCreate_ShouldParseJsonBody_AndSaveNewWhiskyIntoDB() throws Exception {
        Whisky whisky = createWhiskyAndSaveIntoDB();


        // Check we can get the new Whisky resource
        get(WhiskyCrudRestService.REST_WHISKYS_URL + "/" + whisky.getId())
                .then()
                .assertThat()
                .statusCode(WhiskyCrudRestService.STATUS_CODE_OK)
                .body("id", equalTo((int) whisky.getId()))
                .body("name", equalTo(BOURBON))
                .body("origin", equalTo(USA));
    }

    @Test
    public void testGetById() {
        // Now get by id and check the content
        get(WhiskyCrudRestService.REST_WHISKYS_URL + "/" + 2)
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo("John Walker"))
                .body("id", equalTo(2));
    }

    @Test
    public void testGetAll_ShouldReturnListOfWhisky_WithSize3() throws Exception {
        // Get the list of bottles, ensure it's a success and extract the first id.
        get(WhiskyCrudRestService.REST_WHISKYS_URL)
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", hasItems(1, 2, 3));
    }

    @Test
    public void testUpdate_ShouldFindWhiskyById_AndUpdateWhiskyNameAndOrigin() throws Exception {
        // PUT request with ID in URL and JSON body containing new Whisky
        Whisky whisky = given()
                .body(NAME_BOURBON_ORIGIN_USA_JSON)
                .request().put(WhiskyCrudRestService.REST_WHISKYS_URL + "/" + 1)
                .thenReturn().as(Whisky.class);
        // Check JSON parsed correctly
        assertThat(whisky.getName()).isEqualTo(BOURBON);
        assertThat(whisky.getOrigin()).isEqualTo(USA);

        // Check we can get the new Whisky resource
        get(WhiskyCrudRestService.REST_WHISKYS_URL + "/" + whisky.getId())
                .then()
                .assertThat()
                .statusCode(WhiskyCrudRestService.STATUS_CODE_OK)
                .body("id", equalTo((int) whisky.getId()))
                .body("name", equalTo(BOURBON))
                .body("origin", equalTo(USA));
    }

    @Test
    public void testDelete() throws Exception {
        Whisky whisky = createWhiskyAndSaveIntoDB();

        // Delete the newly created Whisky
        delete(WhiskyCrudRestService.REST_WHISKYS_URL + "/" + whisky.getId())
                .then()
                .assertThat()
                .statusCode(WhiskyCrudRestService.STATUS_CODE_OK_NO_CONTENT);

        // Verify the deleted whisky can no longer be found
        get(WhiskyCrudRestService.REST_WHISKYS_URL + "/" + whisky.getId())
                .then()
                .assertThat()
                .statusCode(WhiskyCrudRestService.STATUS_CODE_NOT_FOUND);
    }

    private Whisky createWhiskyAndSaveIntoDB() {
        // POST request with JSON body containing new Whisky
        Whisky whisky = given()
                .body(NAME_BOURBON_ORIGIN_USA_JSON)
                .request().post(WhiskyCrudRestService.REST_WHISKYS_URL)
                .thenReturn().as(Whisky.class);
        // Check JSON parsed correctly
        assertThat(whisky.getName()).isEqualTo(BOURBON);
        assertThat(whisky.getOrigin()).isEqualTo(USA);
        return whisky;
    }
}