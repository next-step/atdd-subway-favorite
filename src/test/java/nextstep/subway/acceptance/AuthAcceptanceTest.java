package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.domain.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.AuthSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.AuthSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final String INVALID_PASSWORD = "asdfafasdfasdf";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Bearer Auth Fail")
    @Test
    void bearerAuthFail() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, INVALID_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        Map<String, String> params = 깃허브_인증_로그인_요청_파라미터_생성();

        ExtractableResponse<Response> response = 깃허브_인증_로그인_요청(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    public static Map<String, String> 깃허브_인증_로그인_요청_파라미터_생성() {
        Map<String, String> params = new HashMap<>();
        params.put("code", GithubResponses.사용자1.getCode());
        return params;
    }

    @DisplayName("Github Auth fail")
    @Test
    void githubAuthFail() {
        Map<String, String> params = new HashMap<>();
        params.put("code", "code");

        ExtractableResponse<Response> response = 깃허브_인증_로그인_요청(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}