package nextstep.utils.resthelper;

import static io.restassured.RestAssured.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import java.util.Map;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.api.auth.application.dto.TokenRequest;

/**
 * @author : Rene Choi
 * @since : 2024/02/13
 */
public class TokenRequestExecutor extends AbstractRequestExecutor {
	private static final String LOGIN_URL_PATH = "/login/token";

	private static RequestSpecification getRequestSpecification() {
		return given().log().all().contentType(APPLICATION_JSON_VALUE);
	}

	public static ExtractableResponse<Response> loginAndCreateAuthorizationToken(Map<String, String> loginParams) {
		return getRequestSpecification()
			.body(loginParams)
			.when().post(LOGIN_URL_PATH)
			.then().log().all()
			.statusCode(OK.value()).extract();
	}

	public static ExtractableResponse<Response> loginAndCreateAuthorizationToken(TokenRequest tokenRequest) {
		return getRequestSpecification()
			.body(tokenRequest)
			.when().post(LOGIN_URL_PATH)
			.then().log().all()
			.statusCode(OK.value()).extract();
	}

}
