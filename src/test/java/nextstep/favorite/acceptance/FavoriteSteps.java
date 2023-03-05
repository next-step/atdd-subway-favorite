package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteSteps {
    private static final String FAVORITE_URL = "/favorites ";
    private static final String LOCATION = "Location";

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/favorites")
                .then().log().all().extract();

    }

    public static Long 즐겨찾기_생성_응답_식별자(ExtractableResponse<Response> createResponse) {
        return Long.parseLong(createResponse.header(LOCATION).substring(FAVORITE_URL.length()));
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long id) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{id}", id)
                .then().log().all().extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> 즐겨찾기_생성_요청_응답) {
        assertThat(즐겨찾기_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_생성_실패함(ExtractableResponse<Response> 즐겨찾기_생성_요청_응답) {
        assertThat(즐겨찾기_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 즐겨찾기_목록_조회됨(Long 즐겨찾기1, Long 즐겨찾기2, ExtractableResponse<Response> 즐겨찾기_목록_조회_응답) {
        List<Long> 즐겨찾기_목록_조회_결과 = 즐겨찾기_목록_조회_응답.jsonPath().getList("id", Long.class);
        assertAll(
                () -> assertThat(즐겨찾기_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(즐겨찾기_목록_조회_결과).containsExactly(즐겨찾기1, 즐겨찾기2)
        );
    }

    public static void 내_즐겨찾기_목록만_조회됨(Long 내_즐겨찾기, Long 다른_사용자_즐겨찾기, ExtractableResponse<Response> 즐겨찾기_목록_조회_응답) {
        List<Long> 즐겨찾기_목록_조회_결과 = 즐겨찾기_목록_조회_응답.jsonPath().getList("id", Long.class);
        assertAll(
                () -> assertThat(즐겨찾기_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(즐겨찾기_목록_조회_결과).containsAnyOf(내_즐겨찾기),
                () -> assertThat(즐겨찾기_목록_조회_결과).doesNotContain(다른_사용자_즐겨찾기)
        );
    }

    public static void 다른_사용자의_즐겨찾기_삭제_실패(ExtractableResponse<Response> 즐겨찾기_삭제_응답) {
        assertThat(즐겨찾기_삭제_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    public static void 즐겨찾기_삭제됨(Long 교대_양재_즐겨찾기, ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_응답, ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답) {
        List<Long> 즐겨찾기_목록_조회_결과 = 즐겨찾기_목록_조회_요청_응답.jsonPath().getList("id", Long.class);
        assertAll(
                () -> assertThat(즐겨찾기_삭제_요청_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(즐겨찾기_목록_조회_결과).doesNotContain(교대_양재_즐겨찾기).isEmpty()
        );
    }

    public static void 권한없음(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
