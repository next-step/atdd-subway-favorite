package nextstep.favorite.acceptance.step;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.constants.Endpoint;
import nextstep.favorite.dto.request.FavoriteRequest;
import nextstep.support.RestAssuredClient;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class FavoriteStep {

    private static final String FAVORITE_BASE_URL = Endpoint.FAVORITE_BASE_URL.getUrl();

    private static final String accessTokenKey = "accessToken";

    /**
     * <pre>
     * 즐겨찾기 항목을 추가하는 API를 호출하는 함수
     * </pre>
     *
     * @param favoriteRequest 즐겨찾기 추가 요청 DTO
     * @param response RestAssured를 통해 로그인 API를 호출해서 응답 받은 응답 객체(nullable)
     * @return ExtractableResponse
     */
    public static ExtractableResponse<Response> 즐겨찾기_추가_요청(FavoriteRequest favoriteRequest,
                                                           @Nullable ExtractableResponse<Response> response
    ) {
        String accessToken = Optional.ofNullable(response)
                .map(r -> r.jsonPath().getString(accessTokenKey))
                .orElse(null);

        return RestAssuredClient.post(FAVORITE_BASE_URL, favoriteRequest, accessToken);
    }

    /**
     * <pre>
     * 즐겨찾기 목록을 조회하는 API를 호출하는 함수
     * </pre>
     *
     * @param response RestAssured를 통해 로그인 API를 호출해서 응답 받은 응답 객체(nullable)
     * @return ExtractableResponse
     */
    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(@Nullable ExtractableResponse<Response> response) {
        String accessToken = Optional.ofNullable(response)
                .map(r -> r.jsonPath().getString(accessTokenKey))
                .orElse(null);

        return RestAssuredClient.get(FAVORITE_BASE_URL, accessToken);
    }

    /**
     * <pre>
     * 즐겨찾기 항목을 삭제하는 API를 호출하는 함수
     * </pre>
     *
     * @param favoriteId 삭제할 즐겨찾기 ID
     * @param response RestAssured를 통해 로그인 API를 호출해서 응답 받은 응답 객체(nullable)
     * @return ExtractableResponse
     */
    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long favoriteId,
                                                           @Nullable ExtractableResponse<Response> response
    ) {
        String accessToken = Optional.ofNullable(response)
                .map(r -> r.jsonPath().getString(accessTokenKey))
                .orElse(null);
        String uri = String.format("%s/%d", FAVORITE_BASE_URL, favoriteId);

        return RestAssuredClient.post(uri, accessToken);
    }
}
