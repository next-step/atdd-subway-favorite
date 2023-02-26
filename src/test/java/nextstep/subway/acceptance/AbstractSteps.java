package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class AbstractSteps {
    public static RequestSpecification oauth2(String accessToken) {
        return given().auth().oauth2(accessToken);
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
