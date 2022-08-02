package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class AuthSteps {
    public static RequestSpecification auth(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken);
    }
}
