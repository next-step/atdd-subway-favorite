package nextstep.subway.favorite.acceptance.step;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceStep {
    public static ExtractableResponse<Response> 즐겨찾기_등록되어_있음(Long source, Long target) {
        return 즐겨찾기_생성을_요청(source, target);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source + "");
        params.put("target", target + "");

        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                body(params).
                when().
                post("/favorites").
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/favorites").
                then().
                log().all().
                extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured.given().log().all().
                when().
                delete(uri).
                then().
                log().all().
                extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 내_즐겨찾기_생성_요청(TokenResponse loginToken, Map<String, String> createFavoriteMap) {
        return RestAssured.given().log().all()
                .auth().oauth2(loginToken.getAccessToken())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(createFavoriteMap)
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_즐겨찾기_목록_조회_요청(TokenResponse loginToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(loginToken.getAccessToken())
                .get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 내_즐겨찾기_삭제_요청(TokenResponse loginToken, ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");

        return RestAssured.given().log().all()
                .auth().oauth2(loginToken.getAccessToken())
                .delete(uri)
                .then()
                .log().all()
                .extract();
    }
}
