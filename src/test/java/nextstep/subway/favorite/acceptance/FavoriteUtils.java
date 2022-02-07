package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.commons.AssertionsUtils;
import org.assertj.core.api.Assertions;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.commons.AssertionsUtils.생성요청_성공;
import static nextstep.subway.commons.RestAssuredUtils.postWithToken;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteUtils {

    private FavoriteUtils() {}

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, long source, long target) {
        Map<String, String> params = new HashMap<>();
        params.put("source", String.valueOf(source));
        params.put("target", String.valueOf(target));

        return postWithToken("/favorites", params , accessToken);
    }

    public static void 즐겨찾기_생성_성공(ExtractableResponse<Response> response) {
        생성요청_성공(response);
    }
}
