package nextstep.subway.acceptance.model;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

public final class FavoriteEntitiesHelper {

    private static final String REQUEST_URI = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .body(newFavorite(source, target))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(REQUEST_URI)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(CREATED.value());
        // TODO Check Elements In Response Body
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .get(REQUEST_URI)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(OK.value());
        // TODO Check Elements In Response Body
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, String uri) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
        // TODO Check Elements In Response Body
    }

    private static Map<String, Long> newFavorite(Long source, Long target) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        return params;
    }
}
