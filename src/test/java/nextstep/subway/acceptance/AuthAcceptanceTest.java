package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.*;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 30;

    @Autowired
    private DataLoader dataLoader;

    @DisplayName("Bearer Auth : Bearer 인증 테스트")
    @Test
    void bearerAuth() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @Test
    @DisplayName("Bearer Auth : Bearer 인증 실패 테스트")
    void bearerAuthFailure() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        assertThatThrownBy(() -> 베어러_인증_로그인_요청(PASSWORD, EMAIL), new NullPointerException().getMessage());
    }

    /**
     * given Github 의 회원일 때
     * when Github 로그인을 하면
     * then 토큰을 발급 받을 수 있다.
     */
    @ParameterizedTest
    @DisplayName("로그인 시 토큰 발급")
    @ValueSource(strings = {"1231@3123", "3123@1231"})
    void singInTest(String code) {
        // given
        dataLoader.loadGithubMemberData();

        // when
        String accessToken = 깃허브_로그인(code).jsonPath().get("accessToken");

        // then
        assertThat(accessToken).isNotBlank();
    }

    /**
     * given Github 의 회원이 아닐 때
     * when Github 로그인을 하면
     * then 토큰을 발급 받을 수 있다.
     */
    @ParameterizedTest
    @DisplayName("회원가입 진행 후 토큰 발급")
    @ValueSource(strings = {"1231@3123", "3123@1231"})
    void singUpTest(String code) {
        // given

        // when
        String accessToken = 깃허브_로그인(code).jsonPath().get("accessToken");

        // then
        assertThat(accessToken).isNotBlank();
    }
}