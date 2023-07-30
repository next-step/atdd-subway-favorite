package nextstep.auth.acceptance;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.token.TokenRequest;

public class AuthSteps {
	public static ExtractableResponse<Response> 일반_로그인_요청(String email,
		String password) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new TokenRequest(email, password))
			.when().post("/login/token")
			.then().log().all().extract();
	}
}
