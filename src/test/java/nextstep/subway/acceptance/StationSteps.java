package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class StationSteps {
	public static ExtractableResponse<Response> 지하철역_생성_요청(String name, String accessToken) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		return given(accessToken)
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/stations")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철역_조회(String accessToken) {
		return given(accessToken)
			.when().get("/stations")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 지하철역_삭제(String uri, String accessToken) {
		return given(accessToken)
			.when().delete(uri)
			.then().log().all()
			.extract();
	}
}
