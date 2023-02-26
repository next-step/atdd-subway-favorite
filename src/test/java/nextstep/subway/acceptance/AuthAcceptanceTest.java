package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.message.Message;
import nextstep.member.domain.stub.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.인가서버에_토큰_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Bearer Auth : 정상")
    @Test
    void bearerAuth() {
        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, 3);

        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Bearer Auth : invalid email")
    @Test
    void bearer_auth_invalid_email() {
        // given
        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, 3);

        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청("invalid", PASSWORD);

        // then
        assertThat(response.body().asString()).contains("존재하지 않는 Email");
    }

    @DisplayName("Bearer Auth : invalid password")
    @Test
    void bearer_auth_invalid_password() {
        // given
        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, 3);

        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, "invalid");

        // then
        assertThat(response.body().asString()).contains("잘못된 비밀번호");
    }
    
    @Test
    @DisplayName("깃헙 로그인 : 정상")
    void github_login_success() {
        // given
        GithubResponses 사용자1 = GithubResponses.사용자1;
        // when
        ExtractableResponse<Response> response = 인가서버에_토큰_요청(사용자1.getCode());
        // then
        assertThat(response.jsonPath().getString("accessToken")).isEqualTo(사용자1.getAccessToken());
    }

    @Test
    @DisplayName("깃헙 로그인 : 존재하지 않는 코드")
    void github_login_fail() {
        // given
        // when
        ExtractableResponse<Response> response = 인가서버에_토큰_요청("Invalid");
        // then
        assertThat(response.body().asString()).contains(Message.INVALID_CODE);
    }

}