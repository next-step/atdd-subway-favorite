package nextstep.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.Steps;

public class FavoriteSteps extends Steps {
	public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
		Map<String, String> params = new HashMap<>();
		params.put("source", source + "");
		params.put("target", target + "");

		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post("/favorites ")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites ")
			.then().log().all().extract();
	}
}
