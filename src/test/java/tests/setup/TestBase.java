package tests.setup;

import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import tests.utils.ConfigLoader;

public class TestBase {
    private static final Logger log = LoggerFactory.getLogger(TestBase.class);
    private static final String baseUri = ConfigLoader.get("baseUri");

    @BeforeMethod(alwaysRun = true)
    public void setup(){
        RestAssured.baseURI = baseUri;
        //RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

    }
}
