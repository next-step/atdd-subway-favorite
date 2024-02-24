package nextstep.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;

public class TokenProvider {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    public static String getAccessToken() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        TokenRequest request = new TokenRequest(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        return "bearer " + response.jsonPath().getString("accessToken");
    }
}
