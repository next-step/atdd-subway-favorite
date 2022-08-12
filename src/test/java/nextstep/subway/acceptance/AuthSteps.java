package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;

public class AuthSteps {
    protected static RequestSpecification authGiven() {
        String adminToken = 로그인_되어_있음("admin@email.com", "password");

        return RestAssured.given().log().all()
                .auth().oauth2(adminToken);
    }
}
