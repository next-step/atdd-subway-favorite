package nextstep.subway.acceptance;

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

    protected static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Map<String, String> favoriteParam) {
        return AcceptanceStep.oAuthRequest(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(favoriteParam)
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
}
