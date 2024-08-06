package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class MemberAssuredTemplate {

    public static Response 자기자신정보요청(String accessToken) {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when()
                .get("/members/me");
    }
}
