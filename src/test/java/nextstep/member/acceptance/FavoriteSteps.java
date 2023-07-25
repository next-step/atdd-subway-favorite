package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static nextstep.utils.AcceptanceTestUtils.*;

public class FavoriteSteps {

    public static ExtractableResponse<Response> 즐겨찾기_생성(Long source, Long target, String accessToken) {
        Map<String, String> params = getCreateFavoriteRequest(source, target);
        return post("/favorites", params, accessToken);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성(Long source, Long target) {
        Map<String, String> params = getCreateFavoriteRequest(source, target);
        return post("/favorites", params);
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(String accessToken) {
        return getOauth2("/favorites", accessToken);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(String path, String accessToken) {
        return delete(path, accessToken);
    }

    private static Map<String, String> getCreateFavoriteRequest(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        params.put("target", target.toString());
        return params;
    }
}
