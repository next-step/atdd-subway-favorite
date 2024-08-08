package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class GithubOauth2ApiRequest{

    public static Response 로그인_한다(final String code) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(Map.of("code", code))
                .basePath("/login/code/github")
                .when().get()
                .then().log().all()
                .extract().response();
    }
}
