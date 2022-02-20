package nextstep.subway.acceptance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class FavoriteSteps {
	public static ExtractableResponse<Response> 즐겨찾기_생성(Long sourceId, Long targetId, String accessToken) {
		Map<String, String> params = new HashMap<>();
		params.put("sourceId", sourceId+"");
		params.put("targetId", targetId+"");
		return RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/favorites")
			.then().log().all().extract();
	}
}
