package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class FavoriteStep {

    protected static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token) {
        ExtractableResponse<Response> result = AcceptanceStep.oAuthRequest(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/favorites")
            .then().log().all().extract();
        return result;
    }

    protected static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Map<String, String> favoriteParam) {
        ExtractableResponse<Response> response = AcceptanceStep.oAuthRequest(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(favoriteParam)
            .when()
            .post("/favorites")
            .then().log().all().extract();
        return response;
    }
}
