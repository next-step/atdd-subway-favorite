package nextstep.study;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

public class AuthSteps {

    public static ExtractableResponse<Response> 깃헙_로그인(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/login/github")
                .then().log().all()
                .extract();
    }

    public static void 상태코드_확인(ExtractableResponse<Response> response, int expected) {
        assertThat(response.statusCode()).isEqualTo(expected);
    }

    public static void accesstoken_발급_검증(ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> assertThat(response.jsonPath().getString("accessToken")).isNotBlank(),
                () -> 상태코드_확인(response, 200)
        );
    }

}
