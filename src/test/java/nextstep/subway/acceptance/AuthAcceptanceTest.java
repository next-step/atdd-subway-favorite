package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.깃헙_소셜_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When Github 권한증서(code) 를 이용하여 로그인을 요청하면
     * Then 내부 서버 사용자에 대한 인증 토큰이 발급된다.
     */
    @DisplayName("Github OAuth")
    @Test
    void githubOAuth() {
        // when
        final ExtractableResponse<Response> 깃헙_소셜_로그인_응답 = 깃헙_소셜_로그인_요청("testCode");

        // then
        assertThat(깃헙_소셜_로그인_응답.jsonPath().getString("accessToken")).isNotBlank();
    }
}