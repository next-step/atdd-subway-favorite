package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class RestGivenWithOauth2 {
    public static RequestSpecification from(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken);
    }

}
