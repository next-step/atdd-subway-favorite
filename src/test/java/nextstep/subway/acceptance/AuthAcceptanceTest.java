package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static nextstep.subway.acceptance.AuthSteps.Github_로그인_요청;
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

    @DisplayName("회원이 Github 로그인을 하는 경우")
    @ParameterizedTest
    @ValueSource(strings = {"832ovnq039hfjn", "mkfo0aFa03m", "m-a3hnfnoew92", "nvci383mciq0oq"})
    void githubAuth(String code) {
        ExtractableResponse<Response> response = Github_로그인_요청(code);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("회원이 아닌 유저가 Github 로그인을 하는 경우")
    @Test
    void githubAuthWhenNotMember() {
        String code = String.valueOf(new Random().nextInt(1000000));
        ExtractableResponse<Response> response = Github_로그인_요청(code);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
