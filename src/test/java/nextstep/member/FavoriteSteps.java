package nextstep.member;

import static org.assertj.core.api.Assertions.*;

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
			.when().post("/favorites")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 비로그인_즐겨찾기_생성_요청(Long source, Long target) {
		Map<String, String> params = new HashMap<>();
		params.put("source", source + "");
		params.put("target", target + "");

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post("/favorites")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.when().delete("/favorites/{favoriteId}", favoriteId)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
		String uri = response.header("Location");
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.when().delete(uri)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 비로그인_즐겨찾기_삭제_요청(ExtractableResponse<Response> response) {
		String uri = response.header("Location");
		return RestAssured.given().log().all()
			.when().delete(uri)
			.then().log().all().extract();
	}

	public static void 즐겨찾기_조회_출발역_확인(ExtractableResponse<Response> response, Long ...stationIds) {
		assertThat(response.jsonPath().getList("source.id", Long.class)).containsExactly(stationIds);
	}
}
