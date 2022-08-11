package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class FavoriteStep {

    protected static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
        return AcceptanceStep.oAuthRequest(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/favorites")
            .then().log().all().extract();
    }

    protected static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token, Long favoriteId) {
        return AcceptanceStep.oAuthRequest(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/favorites/{id}", favoriteId)
            .then().log().all().extract();
    }

    protected static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long source, Long target) {
        return AcceptanceStep.oAuthRequest(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(Map.of("source", source, "target", target))
            .when()
            .post("/favorites")
            .then().log().all().extract();
    }

    protected static ExtractableResponse<Response> 로그인하지_않고_즐겨찾기_생성_요청(Long source, Long target) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(Map.of("source", source, "target", target))
            .when()
            .post("/favorites")
            .then().log().all().extract();
    }

    protected static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, long favoriteId) {
        return AcceptanceStep.oAuthRequest(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("/favorites/{id}", favoriteId)
            .then().log().all().extract();
    }

    protected static ExtractableResponse<Response> 로그인하지_않고_즐겨찾기_삭제_요청(long favoriteId) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("/favorites/{id}", favoriteId)
            .then().log().all().extract();
    }
}
