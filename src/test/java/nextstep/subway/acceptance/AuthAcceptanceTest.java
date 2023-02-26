package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        회원_생성_요청(EMAIL, PASSWORD, 20);
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github Auth")
    @ParameterizedTest
    @ValueSource(strings = {
        "832ovnq039hfjn",
        "mkfo0aFa03m",
        "m-a3hnfnoew92",
        "nvci383mciq0oq"
    })
    void githubAuth(String code) {
        final ExtractableResponse<Response> response = 깃허브_인증_로그인_요청(code);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
