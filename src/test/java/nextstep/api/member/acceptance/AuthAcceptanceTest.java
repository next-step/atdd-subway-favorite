package nextstep.api.member.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.api.CommonAcceptanceTest;
import nextstep.api.member.domain.Member;
import nextstep.api.member.domain.MemberRepository;

class AuthAcceptanceTest extends CommonAcceptanceTest {
	public static final String EMAIL = "admin@email.com";
	public static final String PASSWORD = "password";
	public static final Integer AGE = 20;

	@Autowired
	private MemberRepository memberRepository;

	@DisplayName("Bearer Auth")
	@Test
	void bearerAuth() {
		memberRepository.save(new Member(EMAIL, PASSWORD, AGE));

		Map<String, String> params = new HashMap<>();
		params.put("email", EMAIL);
		params.put("password", PASSWORD);

		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post("/login/token")
			.then().log().all()
			.statusCode(HttpStatus.OK.value()).extract();

		String accessToken = response.jsonPath().getString("accessToken");
		assertThat(accessToken).isNotBlank();

		ExtractableResponse<Response> response2 = RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.when().get("/members/me")
			.then().log().all()
			.statusCode(HttpStatus.OK.value()).extract();

		assertThat(response2.jsonPath().getString("email")).isEqualTo(EMAIL);
	}
}