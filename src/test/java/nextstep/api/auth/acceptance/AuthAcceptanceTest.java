package nextstep.api.auth.acceptance;

import static nextstep.api.member.acceptance.MemberSteps.*;
import static nextstep.fixture.GithubOAuthFixtureCreator.*;
import static nextstep.utils.GithubMockResponses.*;
import static nextstep.utils.resthelper.ExtractableResponseParser.*;
import static nextstep.utils.resthelper.LoginRequestExecutor.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.api.CommonAcceptanceTest;
import nextstep.api.auth.domain.dto.inport.GithubCodeResponse;

/**
 * @author : Rene Choi
 * @since : 2024/02/17
 */
public class AuthAcceptanceTest extends CommonAcceptanceTest {

	/**
	 * Request
	 * POST /login/github HTTP/1.1
	 * content-type: application/json
	 * host: localhost:8080
	 * <p>
	 * {
	 * "code": "qwerasdfzxvcqwerasdfzxcv"
	 * }
	 * <p>
	 * <p>
	 * Response
	 * {
	 * "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjcyNjUyMzAwLCJleHAiOjE2NzI2NTU5MDAsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX0FETUlOIl19.uaUXk5GkqB6QE_qlZisk3RZ3fL74zDADqbJl6LoLkSc"
	 * }
	 */
	@DisplayName("리다이렉트 요청에 따라 깃허브 Response(Token)이 주어질 때 해당 토큰을 처리하여 본 서버의 엑세스 토큰으로 응답한다 - 동일한 email로 이미 가입되어 있는 경우 정상 응답")
	@Test
	void generalGithubAuth_Success() {
		//given
		회원_생성_요청(사용자1);
		GithubCodeResponse githubCodeResponse = createDefaultGithubCodeResponse(사용자1);

		// when
		ExtractableResponse<Response> response = githubLoginWithOk(githubCodeResponse);

		// then
		assertThat(parseSimpleAccessToken(response)).isNotBlank();
	}

	@DisplayName("리다이렉트 요청에 따라 깃허브 Response(Token)이 주어질 때 해당 토큰을 처리하여 본 서버의 엑세스 토큰으로 응답한다 - 유저를 찾을 수 없는 경우 새로운 유저를 만들어서 저장함")
	@Test
	void generalGithubAuth_Fail() {
		//given
		GithubCodeResponse githubCodeResponse = createDefaultGithubCodeResponse(사용자1);

		// when
		ExtractableResponse<Response> response = githubLoginWithOk(githubCodeResponse);

		// then
		assertThat(parseSimpleAccessToken(response)).isNotBlank();
		assertEquals(사용자1.getEmail(), parseEmail(내_정보_조회_요청(parseSimpleAccessToken(response))));
	}

}
