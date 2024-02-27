package nextstep.subway.utils.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.RestApiRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

import java.util.Map;

public class PathSteps {
	private static final String PATH_API_URL = "/paths";

	private static final RestApiRequest<PathRequest> apiRequest = new RestApiRequest<>();

	public static ExtractableResponse<Response> 최단_경로_조회_요청(Long source, Long target) {
		return apiRequest.get(PATH_API_URL, Map.of("source", source, "target", target));
	}
}
