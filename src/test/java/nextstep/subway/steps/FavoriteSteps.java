package nextstep.subway.steps;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class FavoriteSteps {

	public static ExtractableResponse<Response> createFavorite(String accessToken, Long source, Long target) {
		Map<String, String> params = new HashMap<>();
		params.put("source", source + "");
		params.put("target", target + "");

		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post("/favorites")
			.then().log().all()
			.extract();
		return response;
	}

	public static ExtractableResponse<Response> showFavorite(String accessToken) {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/favorites")
			.then().log().all()
			.extract();
		return response;
	}

	public static ExtractableResponse<Response> deleteFavorite(String accessToken, ExtractableResponse<Response> response) {
		String uri = response.header("Location");

		ExtractableResponse<Response> deleteResponse = RestAssured
			.given().log().all()
			.auth().oauth2(accessToken)
			.when().delete(uri)
			.then().log().all()
			.extract();
		return deleteResponse;
	}

	public static void 즐겨찾기_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static void 즐겨찾기_목록_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 즐겨찾기_삭제_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
