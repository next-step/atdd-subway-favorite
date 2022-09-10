package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class Steps {

    protected static RequestSpecification given(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token);
    }
}
