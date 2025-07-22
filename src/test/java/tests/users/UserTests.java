package tests.users;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.setup.TestBase;
import tests.utils.ConfigLoader;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

public class UserTests extends TestBase {

    private static final Logger log = LoggerFactory.getLogger(UserTests.class);
    private final Faker faker = new Faker();
    private final String groupName = faker.university().name();

    @Test
    public void allUserGroups(){
        log.info("Starting allUserGroups test");
        given()
                .post("/users/getAllUserGroups")
                .then()
                .statusCode(200)
//                .log().body()
        ;
    }

    @Test
    public void whenValidateResponseTime_thenSuccess() {
        log.info("Starting whenValidateResponseTime_thenSuccess test");
        when().get("/users/getAllUserGroups").then().time(lessThan(700L));
    }

    @Test
    public void validAuthenticate(){
        log.info("Starting validAuthenticate test");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("username","dsi");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/authenticate")
                .then()
                .statusCode(200)
//                .log().body()
                ;
    }

    @Test
    public void invalidAuthenticate(){
        log.info("Starting invalidAuthenticate test");
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
//                .log().body()
        ;
    }

    @Test
    public void emptyAuthenticate(){
        log.info("Starting emptyAuthenticate test");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("username","");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/authenticate")
                .then()
//                .log().body()
                .statusCode(400)
                .body(equalTo("Username is required"))
        ;
    }

    @Test
    public void nullAuthenticate(){
        log.info("Starting nullAuthenticate test");
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
//                .log().body()
        ;
    }

    @Test
    public void allUsers(){
        log.info("Starting allUsers test");
        given()
                .post("/users/allUsers")
                .then()
                .statusCode(200)
//                .log().body()
        ;
    }

    @Test
    public void validAddUserGroup(){
        log.info("Starting validAddUserGroup test");
        Map<String, Object> jsonMap = new HashMap<>();
//        log.info("GroupName: {}",groupName);
        jsonMap.put("groupName", groupName);
        jsonMap.put("createdBy", "dsi");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/addUserGroup")
                .then()
                .statusCode(200)
//                .log().body()
                .body("isSuccessful", equalTo(true))
                .body("comment", equalTo("Groupe ajouté avec succès"));
    }

    @Test(enabled = false)
    public void emptyAddUserGroup(){
        log.info("Starting emptyAddUserGroup test");
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
//                .log().body()
                .body("isSuccessful", equalTo(false));
    }

    @Test(enabled = false)
    public void nullAddUserGroup(){ //THIS api is wrong and he will accept null input
        log.info("Starting nullAddUserGroup test");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("groupName", null);
        jsonMap.put("createdBy", "dsi");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/addUserGroup")
                .then()
//                .log().body()
                .statusCode(400)
                .body("isSuccessful", equalTo(false))
        ;
    }

    @Test(
            priority = 1
            , dependsOnMethods = "validAddUserGroup"
    )
    public void duplicateAddUserGroup(){
        log.info("Starting duplicateAddUserGroup test");
        Map<String, Object> jsonMap = new HashMap<>();
//        log.info("GroupName: {}",groupName);
        jsonMap.put("groupName", groupName);
        jsonMap.put("createdBy", "dsi");
        given()
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .when()
                .post("/users/addUserGroup")
                .then()
//                .log().body()
                .statusCode(409)
                .body("isSuccessful", equalTo(false))
//                .body("errorCode ", equalTo("DuplicateKeyException"))
                .body("$", hasEntry("errorCode ", "DuplicateKeyException"))
        ;
    }
}

