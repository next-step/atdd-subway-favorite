package subway.acceptance.token;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.acceptance.AcceptanceTest;
import subway.fixture.member.GithubResponses;
import subway.member.JwtTokenProvider;
import subway.member.Member;
import subway.member.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthAcceptanceTest extends AcceptanceTest {
	public static final String EMAIL = "admin@email.com";
	public static final String PASSWORD = "password";
	public static final Integer AGE = 20;

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

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

		assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
	}

	@DisplayName("Github Auth")
	@Test
	void githubAuth() {
		GithubResponses 사용자1 = GithubResponses.사용자1;
		Map<String, String> params = new HashMap<>();
		params.put("code", 사용자1.getCode());

		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when().post("/login/github")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract();

		String token = jwtTokenProvider.createToken(사용자1.getEmail());
		assertThat(response.jsonPath().getString("accessToken")).isEqualTo(token);
	}
}
