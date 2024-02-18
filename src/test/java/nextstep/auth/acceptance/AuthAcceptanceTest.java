package nextstep.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.core.AcceptanceTest;
import nextstep.core.AcceptanceTestAuthBase;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static nextstep.auth.acceptance.AuthSteps.깃허브_로그인_요청;
import static nextstep.auth.acceptance.AuthSteps.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
public class AuthAcceptanceTest extends AcceptanceTestAuthBase {
    /**
     * When email 과 password 으로 로그인 요청시
     * Then 토큰을 발급 받을 수 있다
     */
    @DisplayName("Email 과 Password 로 토큰을 발급 받을 수 있다.")
    @Test
    void bearerAuth() {
        final ExtractableResponse<Response> response = 로그인_요청(EMAIL, PASSWORD);

        토큰_발급_성공(response);
    }

    /**
     * When Github 로그인 요청시
     * Then 토큰을 발급받을 수 있다.
     */
    @DisplayName("Github 로그인 요청시 토큰을 발급 받을 수 있다.")
    @Test
    void githubAuthSuccess() {
        final ExtractableResponse<Response> response = 깃허브_로그인_요청(GithubResponses.사용자1.getCode());

        토큰_발급_성공(response);
    }

    /**
     * When Github 로그인 요청시
     * When Github 로그인 요청에 실패하면
     * Then 에러가 난다.
     */
    @DisplayName("Github 로그인 요청 실패시 에러가 난다.")
    @Test
    void githubAuthFail() {
        final ExtractableResponse<Response> response = 깃허브_로그인_요청(GithubResponses.잘못된_사용자.getCode());

        토큰_발급_실패(response);
    }

    private static void 토큰_발급_성공(final ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    private void 토큰_발급_실패(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
