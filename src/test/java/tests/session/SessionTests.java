package tests.session;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import tests.setup.TestBase;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SessionTests extends TestBase {

    private static final Logger log = LoggerFactory.getLogger(SessionTests.class);
    private final Faker faker = new Faker();

    @Test
    public void signup(){
        log.info("Starting signup test");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("username",faker.name().username());
        jsonMap.put("fullName",faker.name().fullName());
        jsonMap.put("grants","invoice_validation,create_campaign,manage_templates,rejected_bills,manage_users");
        jsonMap.put("role","dsi");
        jsonMap.put("group","1");
//        log.info("jsonMap: {}", jsonMap);
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/session/signup")
                .then()
                .statusCode(200)
//                .log().body()
                .body("isSuccessful", equalTo(true))
                .body("comment", equalTo("Utilisateur ajouté avec succès"))
        ;
    }
}
