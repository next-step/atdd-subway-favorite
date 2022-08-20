package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.auth;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long sourceId, Long targetId) {
        Map<String, String> params = new HashMap<>();
        params.put("sourceId", String.valueOf(sourceId));
        params.put("targetId", String.valueOf(targetId));
        return auth(token)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/favorites")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, String location) {
        return auth(accessToken).log().all()
                .when().delete(location)
                .then().log().all().extract();
    }
    
}
