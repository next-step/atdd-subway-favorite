package nextstep.subway.utils.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.StationRequest;
import nextstep.common.RestApiRequest;

public class StationSteps {
	private static final String STATION_API_URL = "/stations";
	private static final String STATION_WITH_ID_API_URL = "/stations/{id}";
	private static final RestApiRequest<StationRequest> apiRequest = new RestApiRequest<>();

	public static ExtractableResponse<Response> 역_생성_요청(String name) {
		return apiRequest.post(STATION_API_URL, new StationRequest(name));
	}

	public static ExtractableResponse<Response> 역_삭제_요청(Long id) {
		return apiRequest.delete(STATION_WITH_ID_API_URL, id);
	}
}
