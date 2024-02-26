package subway.fixture.acceptance;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
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
