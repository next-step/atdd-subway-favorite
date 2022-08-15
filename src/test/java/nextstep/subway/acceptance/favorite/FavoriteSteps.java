package nextstep.subway.acceptance.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static nextstep.subway.acceptance.favorite.FavoriteAcceptanceTest.NEW_EMAIL;
import static nextstep.subway.acceptance.favorite.FavoriteAcceptanceTest.NEW_PASSWORD;
import static nextstep.subway.acceptance.member.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.member.MemberSteps.어드민_로그인_되어_있음;
import static nextstep.subway.utils.RestAssuredUtils.securedGiven;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 토큰_발급_없이_즐겨찾기_경로_추가_요청(Long source, Long target) {
        return RestAssured.given().log().all()
                .body(Map.of(
                        "source", source,
                        "target", target
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰_발급_및_즐겨찾기_경로_추가_요청(Long source, Long target) {
        return securedGiven(어드민_로그인_되어_있음())
                .body(Map.of(
                        "source", source,
                        "target", target
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_경로_추가_요청(Long source, Long target) {
        ExtractableResponse<Response> response = 토큰_발급_및_즐겨찾기_경로_추가_요청(source, target);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotEmpty();
        return response;
    }

    public static ExtractableResponse<Response> 토큰_발급_및_즐겨찾기_경로_조회_요청(ExtractableResponse<Response> response) {
        return securedGiven(어드민_로그인_되어_있음())
                .when()
                .get(response.header("Location"))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰_발급_없이_즐겨찾기_경로_조회_요청(ExtractableResponse<Response> response) {
        return RestAssured.given().log().all()
                .when()
                .get(response.header("Location"))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 다른_사용자_토큰_발급_및_즐겨찾기_경로_조회_요청(ExtractableResponse<Response> response) {
        return securedGiven(로그인_되어_있음(NEW_EMAIL, NEW_PASSWORD))
                .when()
                .get(response.header("Location"))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰_발급_및_즐겨찾기_경로_삭제_요청(ExtractableResponse<Response> response) {
        return securedGiven(어드민_로그인_되어_있음())
                .when()
                .delete(response.header("Location"))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 토큰_발급_없이_즐겨찾기_경로_삭제_요청(ExtractableResponse<Response> response) {
        return RestAssured.given().log().all()
                .when()
                .delete(response.header("Location"))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 다른_사용자_토큰_발급_및_즐겨찾기_경로_삭제_요청(ExtractableResponse<Response> response) {
        return securedGiven(로그인_되어_있음(NEW_EMAIL, NEW_PASSWORD))
                .when()
                .delete(response.header("Location"))
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_경로_검증(ExtractableResponse<Response> response, String sourceName, String targetName) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getLong("id")).isNotNull(),
                () -> assertThat(response.jsonPath().getString("source.name")).isEqualTo(sourceName),
                () -> assertThat(response.jsonPath().getString("target.name")).isEqualTo(targetName)
        );
    }
}