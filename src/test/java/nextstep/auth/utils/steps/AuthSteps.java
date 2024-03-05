package nextstep.auth.utils.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.application.dto.AuthRequest;
import nextstep.auth.application.dto.GithubAuthRequest;
import nextstep.common.RestApiRequest;

public class AuthSteps {
	private static final String TOKEN_API_URL = "/auth/token";
	private static final RestApiRequest<AuthRequest> apiRequest = new RestApiRequest<>();
	private static final RestApiRequest<GithubAuthRequest> githubApiRequest = new RestApiRequest<>();

	public static ExtractableResponse<Response> 토큰_생성_요청(String email, String password) {
		return apiRequest.post(TOKEN_API_URL, new AuthRequest(email, password));
	}

	public static ExtractableResponse<Response> 깃헙_정보로_토큰_생성_요청(String code) {
		return githubApiRequest.post(TOKEN_API_URL+"/github", new GithubAuthRequest("", "", code));
	}
}
