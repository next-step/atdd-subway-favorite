package nextstep.subway.steps;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AuthSteps {

	public static ExtractableResponse<Response> githubAuth(String code) {
		Map<String, String> params = new HashMap<>();
		params.put("code", code);

		 ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post("/login/github")
			.then().log().all()
			.extract();
		return response;
	}

	public static ExtractableResponse<Response> tokenAuth(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "token " + accessToken);

		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.headers(headers)
			.when().get("/login/github")
			.then().log().all()
			.extract();
		return response;
	}

	public static void github_정상_응답(ExtractableResponse<Response> response, String accessToken) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getString("accessToken")).isEqualTo(accessToken);
	}

	public static void github_실패_응답(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	public static void github_정상_유저정보_응답(ExtractableResponse<Response> response, String email) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getString("email")).isEqualTo(email);
	}
}
