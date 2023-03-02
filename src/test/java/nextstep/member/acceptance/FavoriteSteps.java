package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 비로그인_경로_즐겨찾기_등록_요청(final Long sourceId,
                                                                   final Long targetId) {
        return 경로_즐겨찾기_등록_요청("", sourceId, targetId);
    }

    public static ExtractableResponse<Response> 경로_즐겨찾기_등록_요청(final String token,
                                                              final Long sourceId,
                                                              final Long targetId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static Long 경로_즐겨찾기_등록(final String token,
                                  final Long sourceId,
                                  final Long targetId) {
        final ExtractableResponse<Response> response = 경로_즐겨찾기_등록_요청(token, sourceId, targetId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        final String id = response.header("Location").substring("/favorite/".length());
        return Long.parseLong(id);
    }

    public static ExtractableResponse<Response> 비로그인_즐겨찾기_조회_요청() {
        return 토큰으로_즐겨찾기_조회_요청("");
    }

    public static ExtractableResponse<Response> 토큰으로_즐겨찾기_조회_요청(final String token) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 비로그인_즐겨찾기_삭제_요청(final Long favoriteId) {
        return 토큰으로_즐겨찾기_삭제_요청("", favoriteId);
    }

    public static ExtractableResponse<Response> 토큰으로_즐겨찾기_삭제_요청(final String token, final Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete("/favorites/{id}", favoriteId)
                .then().log().all()
                .extract();
    }
}
