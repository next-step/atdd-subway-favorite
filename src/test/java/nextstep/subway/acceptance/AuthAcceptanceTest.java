package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import nextstep.subway.utils.GithubResponses;

class AuthAcceptanceTest extends AcceptanceTest {
	private static final String EMAIL = "admin@email.com";
	private static final String PASSWORD = "password";
	private static final String WRONG_EMAIL = "wrong@email.com";
	private static final String WRONG_PASSWORD = "wrongpassword";

	@Autowired
	private DataLoader dataLoader;

	@BeforeEach
	public void setUp() {
		super.setUp();
		dataLoader.loadData();
	}

	@DisplayName("Bearer Auth")
	@Test
	void bearerAuth() {
		// when
		ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

		// then
		assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
	}

	@DisplayName("Bearer Auth - 이메일이 틀린 경우 예외를 던진다.")
	@Test
	void bearerAuth_fail_by_email() {
		// when
		ExtractableResponse<Response> response = 베어러_인증_로그인_요청(WRONG_EMAIL, PASSWORD);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}

	@DisplayName("Bearer Auth - 비밀번호가 틀린 경우 예외를 던진다.")
	@Test
	void bearerAuth_fail_by_password() {
		// when
		ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, WRONG_PASSWORD);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}

	@DisplayName("Github Auth")
	@Test
	void githubAuth() {
		// When
		ExtractableResponse<Response> response = github_로그인_요청(GithubResponses.사용자1.getCode());

		// Then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
	}
}
