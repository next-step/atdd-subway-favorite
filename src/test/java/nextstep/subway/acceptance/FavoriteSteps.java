package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.Request;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long target, Long source) {
        Map<String, String> body = new HashMap<>();
        body.put("target", target + "");
        body.put("source", source + "");
        return Request.oauthPost(token, "/favorites", body);
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String token, ExtractableResponse<Response> createResponse) {
        return Request.oauthGet(token, createResponse.header("Location"));
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return Request.oauthGet(token, "/favorites");
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long id) {
        return Request.oauthDelete(token, "/favorites/" + id);
    }
}
