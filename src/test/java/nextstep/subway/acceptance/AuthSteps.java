package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;

public class AuthSteps {

    public static RequestSpecification 인증_사용자_요청() {
        return RestAssured.given().log().all()
            .auth().oauth2(로그인_되어_있음("admin@email.com", "password"));
    }
}
