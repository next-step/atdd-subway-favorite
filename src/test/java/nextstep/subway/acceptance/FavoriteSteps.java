package nextstep.subway.acceptance;

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
    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(String accessToken, Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");

        var given = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        if (accessToken != null) {
            given = given.auth().oauth2(accessToken);
        }

        // when
        return given
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_추가됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 인증_예외_발생함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 권한_예외_발생함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    public static void 찾을_수_없는_예외_발생함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        var given = RestAssured.given().log().all();

        if (accessToken != null) {
            given.auth().oauth2(accessToken);
        }

        return given
                .when()
                .get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_조회됨(ExtractableResponse<Response> response, List<Long> sourceIds, List<Long> targetIds) {
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.body().jsonPath().getList("source.id", Long.class)).containsExactlyElementsOf(sourceIds);
            assertThat(response.body().jsonPath().getList("target.id", Long.class)).containsExactlyElementsOf(targetIds);
        });
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, String location) {
        var given = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        if (accessToken != null) {
            given.auth().oauth2(accessToken);
        }

        return given
                .when()
                .delete(location)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
