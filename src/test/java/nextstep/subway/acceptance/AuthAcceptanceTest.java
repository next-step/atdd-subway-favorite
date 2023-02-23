package nextstep.subway.acceptance;

import static nextstep.member.infrastructure.FakeGithubClientImpl.DummyGithubResponses.사용자1;
import static nextstep.member.infrastructure.FakeGithubClientImpl.DummyGithubResponses.사용자2;
import static nextstep.member.infrastructure.FakeGithubClientImpl.DummyGithubResponses.사용자3;
import static nextstep.member.infrastructure.FakeGithubClientImpl.DummyGithubResponses.사용자4;
import static nextstep.subway.acceptance.AuthSteps.Github_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

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

    @DisplayName("회원이 Github 로그인을 하는 경우")
    @ParameterizedTest
    @ValueSource(strings = {"832ovnq039hfjn", "mkfo0aFa03m", "m-a3hnfnoew92", "nvci383mciq0oq"})
    void githubAuth(String code) {
        회원_등록();
        ExtractableResponse<Response> response = Github_로그인_요청(code);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    private static void 회원_등록() {
        회원_생성_요청(사용자1.getEmail());
        회원_생성_요청(사용자2.getEmail());
        회원_생성_요청(사용자3.getEmail());
        회원_생성_요청(사용자4.getEmail());
    }

    /**
     * Given 회원 가입하지 않은 유저 When  의 코드로 로그인 요청을 하는 경우 Then Github에서 받은 email 정보로 가입시킨 뒤 Code를 반환
     */
    @DisplayName("회원이 아닌 유저가 Github 로그인을 하는 경우")
    @Test
    void githubAuthWhenNotMember() {
        String 회원가입_하지_않은_유저의_Github_코드 = 사용자1.getCode();
        ExtractableResponse<Response> response = Github_로그인_요청(회원가입_하지_않은_유저의_Github_코드);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}
