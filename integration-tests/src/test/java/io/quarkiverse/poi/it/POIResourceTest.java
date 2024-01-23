package io.quarkiverse.poi.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class POIResourceTest {

    @Test
    public void testDocx() {
        given()
                .when().get("/poi/docx")
                .then()
                .statusCode(200)
                .body(startsWith("Hello POI"));
    }

    @Test
    public void testDoc() {
        given()
                .when().get("/poi/doc")
                .then()
                .statusCode(200)
                .body(startsWith("Hello POI"));
    }

    @Test
    public void testXls() {
        given()
                .when().get("/poi/xls")
                .then()
                .statusCode(200)
                .body(is("test name1"));
    }

    @Test
    public void testXlxs() {
        given()
                .when().get("/poi/xlxs")
                .then()
                .statusCode(200)
                .body(is("test"));
    }

    @Test
    public void testXlsx() {
        given()
                .when().get("/poi/xlsx")
                .then()
                .statusCode(200)
                .body(is("Hello POI"));
    }

    @Test
    public void testSpecialFile() {
        given()
                .when().get("/poi/specialFile")
                .then()
                .statusCode(200)
                .body(is("Hallo POI"));
    }
}