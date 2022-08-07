package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class AuthSteps {

    public static String ADMIN_EMAIL = "admin@email.com";
    public static String ADMIN_PASSWORD = "password";
    public static int ADMIN_AGE = 20;

    public static String MEMBER_EMAIL = "member@email.com";
    public static String MEMBER_PASSWORD = "password";
    public static int MEMBER_AGE = 20;

    public static RequestSpecification given(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token);
    }


}
