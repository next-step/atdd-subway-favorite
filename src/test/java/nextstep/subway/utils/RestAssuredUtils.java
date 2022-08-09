package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class RestAssuredUtils {

    public static RequestSpecification securedGiven(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token);
    }

}
