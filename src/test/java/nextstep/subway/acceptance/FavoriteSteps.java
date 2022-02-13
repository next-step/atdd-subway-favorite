package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteSteps {

    public static final String FAVORITES_PATH = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(final String accessToken, final Long source, final Long target) {
        final Map<String, String> params = 즐겨찾기_생성_데이터를_만든다(source, target);
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.ALL_VALUE)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post(FAVORITES_PATH)
                .then().log().all()
                .extract();
    }

    private static Map<String, String> 즐겨찾기_생성_데이터를_만든다(final Long source, final Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        params.put("target", target.toString());
        return params;
    }

    public static ExtractableResponse<Response> 즐겨_찾기_삭제_요청(final String accessToken, final String locationUrl) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.ALL_VALUE)
                .when().delete(locationUrl)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(FAVORITES_PATH)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회_됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
