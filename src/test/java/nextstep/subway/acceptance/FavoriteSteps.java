package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.FavoriteResponse;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(final String token, final Long sourceId, final Long targetId) {
        return RestAssured
                .given()
                    .auth().oauth2(token)
                    .accept(ALL_VALUE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .body(Map.of("source", sourceId, "target", targetId))
                .when()
                    .post("/favorites")
                .then()
                    .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 인증없이_즐겨찾기_추가_요청(final Long sourceId, final Long targetId) {
        return RestAssured
                .given()
                    .accept(ALL_VALUE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .body(Map.of("source", sourceId, "target", targetId))
                .when()
                    .post("/favorites")
                .then()
                    .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String token) {
        return RestAssured
                .given()
                    .auth().oauth2(token)
                    .accept(APPLICATION_JSON_VALUE)
                .when()
                    .get("/favorites")
                .then()
                    .log().all()
                .extract();
    }

    public static List<FavoriteResponse> 즐겨찾기_목록_조회_요청하고_목록_반환(final String token) {
        return 즐겨찾기_목록_조회_요청(token)
                .jsonPath()
                .getList("", FavoriteResponse.class);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(final String token, final String path) {
        return RestAssured
                .given()
                    .auth().oauth2(token)
                    .accept(ALL_VALUE)
                .when()
                    .delete(path)
                .then()
                    .log().all()
                .extract();
    }

}
