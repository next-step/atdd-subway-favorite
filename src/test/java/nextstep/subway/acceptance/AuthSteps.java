package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;

public class AuthSteps {
    private static final String ADMIN_EMAIL = "admin@email.com";
    private static final String ADMIN_PASSWORD = "password";

    private static final String MEMBER_EMAIL = "member@email.com";
    private static final String MEMBER_PASSWORD = "password";

    public static RequestSpecification ADMIN_토큰권한으로_호출() {
        return RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(로그인_되어_있음(ADMIN_EMAIL, ADMIN_PASSWORD));
    }

    public static RequestSpecification MEMBER_토큰권한으로_호출() {
        return RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(로그인_되어_있음(MEMBER_EMAIL, MEMBER_PASSWORD));
    }
}
