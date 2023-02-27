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

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회(final String accessToken) {
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
        var result = 즐겨찾기_목록_조회(accessToken);

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
}
