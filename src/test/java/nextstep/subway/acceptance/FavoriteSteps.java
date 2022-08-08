package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.LineSteps.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class FavoriteSteps {
	private static final String BASE_URI = "/favorite";

	public static ExtractableResponse<Response> 즐겨찾기_등록(long source, long target, String accessToken) {
		Map<String, Object> params = new HashMap<>();
		params.put("source", source);
		params.put("target", target);

		return given(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post(BASE_URI)
			.then().log().all()
			.statusCode(HttpStatus.OK.value()).extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제(ExtractableResponse<Response> response, String accessToken) {
		return given(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.delete(response.header("Location"))
			.then().log().all()
			.statusCode(HttpStatus.OK.value()).extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_조회(ExtractableResponse<Response> response) {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get(response.header("Location"))
			.then().log().all()
			.extract();
	}
}
