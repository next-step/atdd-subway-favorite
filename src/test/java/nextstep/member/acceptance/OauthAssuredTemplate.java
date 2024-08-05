package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import nextstep.member.application.dto.OauthTokenRequest;
import org.springframework.http.MediaType;

public class OauthAssuredTemplate {

    public static Response 깃허브로그인(String code) {

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OauthTokenRequest(code))
                .when().post("/login/github");
    }
}
