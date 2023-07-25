package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static nextstep.utils.AcceptanceTestUtils.getOauth2;
import static nextstep.utils.AcceptanceTestUtils.post;

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

    private static Map<String, String> getCreateFavoriteRequest(Long source, Long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        params.put("target", target.toString());
        return params;
    }
}
