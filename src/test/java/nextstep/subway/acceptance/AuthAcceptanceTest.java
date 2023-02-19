package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.GitHubResponses;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("GitHub Auth")
    @Test
    void gitHubAuth() {
        // when
        ExtractableResponse<Response> response = 깃허브_인증_로그인_요청(GitHubResponses.사용자1.getCode());

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
