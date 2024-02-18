package nextstep.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.RestAssuredHelper;

import java.util.Map;

public class AuthSteps {

    private static final String API_PATH_PREFIX = "/login";

    private AuthSteps() {
    }

    public static ExtractableResponse<Response> 로그인_요청(final String email, final String password) {
        return RestAssuredHelper.post(API_PATH_PREFIX + "/token", Map.of("email", email, "password", password));
    }

    public static ExtractableResponse<Response> 깃허브_로그인_요청(final String code) {
        return RestAssuredHelper.post(API_PATH_PREFIX + "/github", Map.of("code", code));
    }
}
