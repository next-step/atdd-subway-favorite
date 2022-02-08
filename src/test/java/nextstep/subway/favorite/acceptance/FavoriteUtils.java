package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.commons.AssertionsUtils;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.commons.AssertionsUtils.*;
import static nextstep.subway.commons.RestAssuredUtils.*;
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

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return getWithToken("/favorites", accessToken);
    }

    public static void 즐겨찾기_조회_성공(ExtractableResponse<Response> response) {
        조회요청_성공(response);
    }

    public static void 즐겨찾기_목록_개수_검증(ExtractableResponse<Response> response, int size) {
        assertThat(response.jsonPath().getList("id")).hasSize(size);
    }

    public static void 즐겨찾기_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> createResponse) {
        String location = createResponse.header("Location");
        return deleteWithToken(location, accessToken);
    }

    public static void 즐겨찾기_삭제_성공(ExtractableResponse<Response> deleteResponse, ExtractableResponse<Response> findResponse) {
        삭제요청_성공(deleteResponse);
        assertThat(findResponse.jsonPath().getList("id")).hasSize(1);
    }
}
