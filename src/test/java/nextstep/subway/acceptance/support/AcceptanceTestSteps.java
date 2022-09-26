package nextstep.subway.acceptance.support;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class AcceptanceTestSteps {
    public static RequestSpecification given() {
        return RestAssured
            .given().log().all();
    }

    public static RequestSpecification given(String token) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(token);
    }
}
