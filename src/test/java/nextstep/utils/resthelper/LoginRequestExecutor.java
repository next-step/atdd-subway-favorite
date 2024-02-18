package nextstep.utils.resthelper;

import static io.restassured.RestAssured.*;
import static org.springframework.http.MediaType.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.client.github.dto.GithubAccessTokenResponse;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
public class LoginRequestExecutor extends AbstractRequestExecutor{

	private static final String LOGIN_URL_PATH = "/login";
	private static final String GITHUB_LOGIN_URL_PATH = LOGIN_URL_PATH + "/github";

	private static RequestSpecification getRequestSpecification() {
		return given().log().all().contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> githubLoginWithOk(GithubAccessTokenResponse githubAccessTokenResponse) {
		return doPostWithOk(getRequestSpecification(), GITHUB_LOGIN_URL_PATH, githubAccessTokenResponse);
	}


}
