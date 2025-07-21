package tests.users;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import tests.utils.ConfigLoader;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserTests {

    private static final Logger log = LoggerFactory.getLogger(UserTests.class);
    private final Faker faker = new Faker();
    private final String groupName = faker.university().name();
    private static final String baseUri = ConfigLoader.get("baseUri");

    @BeforeMethod(alwaysRun = true)
    public void setup(){
        log.info("=== @BeforeMethod executed ===");
        RestAssured.baseURI = baseUri;
    }

    @Test
    public void allUserGroups(){
        given()
                .post("/users/getAllUserGroups")
                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test
    public void whenValidateResponseTime_thenSuccess() {
        when().get("/users/getAllUserGroups").then().time(lessThan(700L));
    }

    @Test
    public void validAuthenticate(){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("username","dsi");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/authenticate")
                .then()
                .statusCode(200)
                .log().body();
    }

    @Test
    public void invalidAuthenticate(){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("username","admin");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/authenticate")
                .then()
                .statusCode(401)
                .body(equalTo("User not found"))
                .log().body();
    }

    @Test
    public void emptyAuthenticate(){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("username","");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/authenticate")
                .then()
                .log().body()
                .statusCode(400)
                .body(equalTo("Username is required"))
        ;
    }

    @Test
    public void nullAuthenticate(){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("username",null);
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/authenticate")
                .then()
                .statusCode(400)
                .body(equalTo("Username is required"))
                .log().body();
    }

    @Test
    public void allUsers(){
        given()
                .post("/users/allUsers")
                .then()
                .statusCode(200)
                .log().body();
    }

    @Test
    public void validAddUserGroup(){
        Map<String, Object> jsonMap = new HashMap<>();
        log.info("GroupName: {}",groupName);
        jsonMap.put("groupName", groupName);
        jsonMap.put("createdBy", "dsi");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/addUserGroup")
                .then()
                .statusCode(200)
                .log().body()
                .body("isSuccessful", equalTo(true))
                .body("comment", equalTo("Groupe ajouté avec succès"));
    }

    @Test(enabled = false)
    public void emptyAddUserGroup(){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("groupName", "");
        jsonMap.put("createdBy", "dsi");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/addUserGroup")
                .then()
                .statusCode(400)
                .log().body()
                .body("isSuccessful", equalTo(false));
    }

    @Test(enabled = false)
    public void nullAddUserGroup(){ //THIS api is wrong and he will accept null input
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("groupName", null);
        jsonMap.put("createdBy", "dsi");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/addUserGroup")
                .then()
                .log().body()
                .statusCode(400)
                .body("isSuccessful", equalTo(false))
        ;
    }

    @Test(
            priority = 1
            , dependsOnMethods = "validAddUserGroup"
    )
    public void duplicateAddUserGroup(){
        Map<String, Object> jsonMap = new HashMap<>();
        log.info("GroupName: {}",groupName);
        jsonMap.put("groupName", groupName);
        jsonMap.put("createdBy", "dsi");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/addUserGroup")
                .then()
                .log().body()
                .statusCode(409)
                .body("isSuccessful", equalTo(false))
//                .body("errorCode ", equalTo("DuplicateKeyException"))
                .body("$", hasEntry("errorCode ", "DuplicateKeyException"))
        ;
    }
}

