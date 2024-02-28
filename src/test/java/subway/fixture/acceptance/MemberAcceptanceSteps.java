package subway.fixture.acceptance;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.member.MemberRequest;
import subway.dto.member.TokenRequest;
import subway.utils.enums.Location;

public class MemberAcceptanceSteps {
	public static final String EMAIL = "admin@email.com";
	public static final String PASSWORD = "password";
	public static final Integer AGE = 20;

	public static void 멤버_생성() {
		RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new MemberRequest(EMAIL, PASSWORD, AGE))
			.when().post(Location.MEMBERS.path())
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value());
	}

	public static ExtractableResponse<Response> 멤버_ME_조회(String accessToken) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.auth().oauth2(accessToken)
			.when().get(Location.MEMBERS.path("/me").toUriString())
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract();
	}

	public static String 로그인() {
		TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tokenRequest)
			.when().post(Location.TOKENS.path())
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract()
			.jsonPath().getString("accessToken");
	}
}
