package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.TokenRequest;
import nextstep.utils.RestApiRequest;

public class LoginSteps {
	private static final String TOKEN_API_URL = "/login";
	private static final RestApiRequest<TokenRequest> apiRequest = new RestApiRequest<>();
	private static final RestApiRequest<GithubAccessTokenRequest> githubApiRequest = new RestApiRequest<>();

	public static ExtractableResponse<Response> 인증정보_생성_요청(String email, String password) {
		return apiRequest.post(TOKEN_API_URL+"/token", new TokenRequest(email, password));
	}

	public static ExtractableResponse<Response> 깃헙_로그인_요청(String code) {
		return githubApiRequest.post(TOKEN_API_URL+"/github", new GithubAccessTokenRequest("", "", code));
	}
}
