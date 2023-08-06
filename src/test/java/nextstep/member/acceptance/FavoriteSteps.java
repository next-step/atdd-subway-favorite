package nextstep.member.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.utils.AuthApiRequest;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSteps {
    private FavoriteSteps() {
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long sourceId, Long targetId) {
        Map<String, String> params = new HashMap<>();
        params.put("source", sourceId + "");
        params.put("target", targetId + "");
        return AuthApiRequest.post(accessToken, "/favorites", params);
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return AuthApiRequest.get(accessToken, "/favorites");
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, String path) {
        return AuthApiRequest.delete(accessToken, path);
    }
}
