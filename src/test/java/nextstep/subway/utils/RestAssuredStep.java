package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

/**
 * @author a1101466 on 2022/07/30
 * @project subway
 * @description
 */
public class RestAssuredStep {
    public static RequestSpecification given(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken);
    }
}
