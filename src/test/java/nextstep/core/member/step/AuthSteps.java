package nextstep.core.member.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthSteps {
    public static String 성공하는_토큰_발급_요청(String 이메일, String 비밀번호) {
        Map<String, String> params = new HashMap<>();
        params.put("email", 이메일);
        params.put("password", 비밀번호);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract();

        String accessToken = response.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();
        return accessToken;
    }

    public static void 실패하는_토큰_발급_요청(String 이메일, String 비밀번호) {
        Map<String, String> params = new HashMap<>();
        params.put("email", 이메일);
        params.put("password", 비밀번호);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value()).extract();
    }
}
