package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.ThrowableAssert.*;

public class FavoriteAcceptanceSteps {

    public static final String FAVORITES_URI = "/favorites";

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("password", target + "");

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(FAVORITES_URI)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(FAVORITES_URI)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(FAVORITES_URI + "/" + favoriteId)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_생성_실패됨(ThrowingCallable actual) {
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    public static void 즐겨찾기_삭제_실패됨(ThrowingCallable actual) {
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, int favoriteCount) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("favorites", Long.class)).hasSize(favoriteCount);
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response, int favoriteCount) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("favorites", Long.class)).hasSize(favoriteCount);
    }

    public static Long 첫_번째_즐겨찾기(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("id", Long.class).get(0);
    }



}
