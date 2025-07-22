package tests.billing;

import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import tests.setup.TestBase;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BillingTests extends TestBase {
    private static final Logger log = LoggerFactory.getLogger(BillingTests.class);

    @Test
    public void getGetBillcycles(){
        log.info("Starting getGetBillcycles test");
        List<Map<String, Object>> billcyclesList =
        given()
                .when()
                .get("/billing/getBillcycles")
                .then()
//                .log().body()
                .statusCode(200)
                .body("isSuccessful", equalTo(true))
                .body("comment", equalTo(null))
                .extract().path("billcycleDetails")
                ;
                log.info("Returned billcycles count: {}", billcyclesList.size());
    }

    @Test
    public void getTypesComments(){
        log.info("Starting getTypesComments test");
        given()
                .when()
                .get("/billing/typesComments")
                .then()
//                .log().body()
                .statusCode(200)
                .body("$",hasKey("types"))
                .body("types",containsInAnyOrder("WARNING","INFO","ERROR"))
        ;
    }

    @Test
    public void getGetCategories(){
        log.info("Starting getGetCategories test");
        List<String> categories =
                given()
                .when()
                .get("/billing/getCategories")
                .then()
//                .log().body()
                .statusCode(200)
                .body("$",hasKey("categoryNames"))
                        .extract().path("categoryNames")
                ;
        log.info("Returned categories count: {}",categories.size());
    }
}
