package nextstep.subway.utils.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.FavoriteRequest;
import nextstep.common.RestApiRequest;

public class FavoriteSteps {
	private static final String FAVORITE_API_URL = "/favorites";
	private static final String FAVORITE_API_WITH_ID_URL = "/favorites/{id}";

	private static final RestApiRequest<FavoriteRequest> apiRequest = new RestApiRequest<>();

	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
		return apiRequest.post(FAVORITE_API_URL, accessToken, new FavoriteRequest(source, target));
	}

	public static ExtractableResponse<Response> 즐겨찾기_전체_조회_요청(String accessToken) {
		return apiRequest.get(FAVORITE_API_URL, accessToken);
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long id) {
		return apiRequest.delete(FAVORITE_API_WITH_ID_URL, accessToken, id);
	}
}
