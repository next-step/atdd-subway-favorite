package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 지하철_즐겨찾기_생성_요청(String token, Map<String, String> params) {
        return RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(params)
                .auth().oauth2(token)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static void 지하철_즐겨찾기_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_즐겨찾기_생성_실패됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 지하철_즐겨찾기_조회_요청(String token) {
        return RestAssured
                .given().log().all().accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static void 지하철_즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_즐겨찾기_삭제_요청(String token, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete("/favorites/{id}", favoriteId)
                .then().log().all().extract();
    }

    public static void 지하철_즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_즐겨찾기_삭제_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}