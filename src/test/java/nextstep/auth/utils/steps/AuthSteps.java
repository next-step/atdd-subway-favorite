package nextstep.auth.utils.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.common.RestApiRequest;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.TokenRequest;

public class AuthSteps {
	private static final String TOKEN_API_URL = "/auth/token";
	private static final RestApiRequest<TokenRequest> apiRequest = new RestApiRequest<>();
	private static final RestApiRequest<GithubAccessTokenRequest> githubApiRequest = new RestApiRequest<>();

	public static ExtractableResponse<Response> 토큰_생성_요청(String email, String password) {
		return apiRequest.post(TOKEN_API_URL, new TokenRequest(email, password));
	}

	public static ExtractableResponse<Response> 깃헙_정보로_토큰_생성_요청(String code) {
		return githubApiRequest.post(TOKEN_API_URL+"/github", new GithubAccessTokenRequest("", "", code));
	}
}
