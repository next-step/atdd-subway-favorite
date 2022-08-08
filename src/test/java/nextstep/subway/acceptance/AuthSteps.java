package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;

public class AuthSteps {
    public static RequestSpecification given() {
        var token = 로그인_되어_있음("admin@nextstep.com", "12345");
        return RestAssured
                .given().log().all()
                .auth()
                .oauth2(token);
    }
}
