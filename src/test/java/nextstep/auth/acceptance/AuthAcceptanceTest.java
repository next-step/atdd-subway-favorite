package nextstep.auth.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import nextstep.auth.acceptance.fake.FakeGithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.acceptance.AuthSteps.깃헙_로그인_요청_성공;
import static nextstep.auth.acceptance.AuthSteps.깃헙_로그인_요청_실패;
import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청_성공;
import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청_실패;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    /**
     * given 회원이 있을 때
     * when 회원의 이메일과 비밀번호로 로그인 요청하면
     * then 해당 회원에 대한 토큰을 발급받는다.
     */
    @DisplayName("이메일과 비밀번호로 토큰을 요청한다.")
    @Test
    void bearerAuthLogin() {
        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청_성공(EMAIL, PASSWORD);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * given 회원이 있을 때
     * when 회원의 이메일과 다른 이메일로 로그인 요청하면
     * then 토큰을 발급받을 수 없다.
     */
    @DisplayName("일치하지 않는 이메일로 로그인 요청하면 토큰을 발급받을 수 없다.")
    @Test
    void bearerAuthLoginExceptionNotMatchEmail() {
        // given
        final String invalidEmail = "admin1@email.com";

        // when & then
        베어러_인증_로그인_요청_실패(invalidEmail, PASSWORD);
    }

    /**
     * given 회원이 있을 때
     * when 회원의 비밀번호와 다른 비밀번호로 로그인 요청하면
     * then 토큰을 발급받을 수 없다.
     */
    @DisplayName("Bearer Auth : 일치하지 않는 비밀번호로 로그인 요청하면 토큰을 발급받을 수 없다.")
    @Test
    void bearerAuthLoginExceptionNotMatchPassword() {
        // given
        final String invalidPassword = "password1";

        // when & then
        베어러_인증_로그인_요청_실패(EMAIL, invalidPassword);
    }

    /**
     * given Github 에서 받은, 서버에 이미 등록된 유저의 권한증서(code)를 가지고
     * when Github 로 로그인 요청하면
     * then 우리 서버의 토큰을 발급받을 수 있다.
     */
    @DisplayName("Github Auth : Github 에서 받은 이미 등록된 유저의 권한증서(code)를 이용하여 로그인 요청 하면 액세스 토큰을 받는다.")
    @ParameterizedTest
    @ValueSource(strings = {"사용자1", "사용자2", "사용자3", "사용자4"})
    void githubAuthLoginAlreadyMember(FakeGithubResponses fakeGithubResponses) {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("code", fakeGithubResponses.getCode());

        // when
        ExtractableResponse<Response> response = 깃헙_로그인_요청_성공(params);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * given Github 에서 받은, 서버에 등록되지 않은 유저의 권한증서(code)를 가지고
     * when Github 로 로그인 요청하면
     * then 우리 서버의 토큰을 발급받을 수 있다.
     */
    @DisplayName("Github Auth : Github 에서 받은 서버에 등록되지 않은 유저의 권한증서(code)를 이용하여 로그인 요청 하면 액세스 토큰을 받는다.")
    @Test
    void githubAuthLoginCreateMember() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("code", FakeGithubResponses.비회원.getCode());

        // when
        ExtractableResponse<Response> response = 깃헙_로그인_요청_성공(params);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * given Github 에 없는 권한증서(code)를 가지고
     * when Github 로 로그인 요청하면
     * then 우리 서버의 토큰을 발급받을 수 없다.
     */
    @DisplayName("Github Auth : Github 에 없는 권한증서(code)로 로그인 요청하면 토큰을 발급받을 수 없다.")
    @Test
    void githubAuthLoginExceptionNotContainsCode() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("code", "code");

        // when & then
        깃헙_로그인_요청_실패(params);
    }
}