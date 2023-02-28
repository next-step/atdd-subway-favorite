package nextstep.member.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;

public class FavoriteAcceptanceSteps {

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(
            final String accessToken,
            final Long source,
            final Long target
    ) {
        Map<String, Long> body = Map.of(
                "source", source,
                "target", target
        );

        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .when()
                .body(body)
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .auth().oauth2(accessToken)
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_등록_검증(
            final String accessToken,
            final ExtractableResponse<Response> actualResponse,
            final int size
    ) {
        var result = 즐겨찾기_목록_조회_요청(accessToken);

        Assertions.assertAll(
                () -> assertThat(actualResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(result.jsonPath().getList("id", Long.class)).hasSize(size)
        );
    }

    public static void 연결되지_않은_역으로_즐겨찾기를_등록하면_예외_처리한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 존재하지_않은_역으로_즐겨찾기를_등록하면_예외_처리한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 즐겨찾기_목록_조회_검증(
            final ExtractableResponse<Response> response,
            final Long source,
            final Long target
    ) {
        JsonPath jsonPath = response.jsonPath();

        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getList("id")).hasSize(1),
                () -> assertThat(jsonPath.getLong("source[0].id")).isEqualTo(source),
                () -> assertThat(jsonPath.getLong("target[0].id")).isEqualTo(target)
        );
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(final String accessToken, final String location) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .delete(location)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제_검증(final ExtractableResponse<Response> response) {
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(response.jsonPath().getList("id")).hasSize(0)
        );
    }

    public static void 자신의_즐겨찾기_목록에_등록되지_않은_ID로_삭제하면_예외처리한다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 유효하지_않은_토큰으로_즐겨찾기를_등록하면_예외_처리한다(
            final String accessToken,
            final Long source,
            final Long target
    ) {
        assertThat(즐겨찾기_등록_요청(accessToken, source, target).statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 유효하지_않은_토큰으로_즐겨찾기를_조회하면_예외_처리한다(final String accessToken) {
        assertThat(즐겨찾기_목록_조회_요청(accessToken).statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 유효하지_않은_토큰으로_즐겨찾기를_삭제하면_예외_처리한다(final String accessToken, final String location) {
        assertThat(즐겨찾기_삭제_요청(accessToken, location).statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 토큰_없이_즐겨찾기를_등록하면_예외_처리한다(final Long source, final Long target) {
        Map<String, Long> body = Map.of(
                "source", source,
                "target", target
        );

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .body(body)
                .post("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 토큰_없이_즐겨찾기를_조회하면_예외_처리한다() {
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/favorites")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    public static void 토큰_없이_즐겨찾기를_삭제하면_예외_처리한다(final String location) {
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .delete(location)
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
