package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.TokenRequest;
import nextstep.utils.RestApiRequest;

public class TokenSteps {
	private static final String TOKEN_API_URL = "/login/token";
	private static final RestApiRequest<TokenRequest> apiRequest = new RestApiRequest<>();

	public static ExtractableResponse<Response> 인증정보_생성_요청(String email, String password) {
		return apiRequest.post(TOKEN_API_URL, new TokenRequest(email, password));
	}
}
