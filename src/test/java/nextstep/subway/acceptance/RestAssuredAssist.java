package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class RestAssuredAssist {
    public static RequestSpecification given(String accessToken) {
        return RestAssured.given()
                .auth().oauth2(accessToken).log().all();
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }
}
