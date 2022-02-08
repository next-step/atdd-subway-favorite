package nextstep.subway.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, long source, long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static void 즐겨찾기_생성_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 권한_없음_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
