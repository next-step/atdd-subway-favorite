package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final String CODE = "code";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github Auth")
    @Test
    void githubAuth() {
        ExtractableResponse<Response> response = 깃허브_인증_로그인_요청(CODE);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
