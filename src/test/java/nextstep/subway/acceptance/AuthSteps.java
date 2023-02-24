package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class AuthSteps {
	public static ExtractableResponse<Response> 깃헙_로그인_요청(String code) {
		Map<String, String> params = new HashMap<>();
		params.put("code", code);
		return RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(params)
				.when().post("/login/github")
				.then().log().all().extract();
	}
}
