package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.specification.RequestSpecification;

public class AcceptanceSteps {

    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    public static RequestSpecification given(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token);
    }

    public static RequestSpecification given(String email, String password, String formAction) {
        return RestAssured.given().log().all()
                .auth().form(email, password, new FormAuthConfig(formAction, USERNAME_FIELD, PASSWORD_FIELD));
    }

    public static RequestSpecification given(String username, String password) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(username, password);
    }
}
