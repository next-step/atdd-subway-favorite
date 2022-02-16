package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {
    public static ExtractableResponse<Response> 로그인_지하철_즐겨찾기_생성_요청(Long source, Long target, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        params.put("target", target.toString());

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 비로그인_지하철_즐겨찾기_생성_요청(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        params.put("target", target.toString());

        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all().extract();
    }

    public static void 지하철_즐겨찾기_생성_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 비로그인_지하철_즐겨찾기_생성_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 즐겨찾기_중복_생성_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
