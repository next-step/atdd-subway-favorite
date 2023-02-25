package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import nextstep.utils.DataLoader;
import nextstep.member.ui.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.LoginSteps.베어러_인증_로그인_실패하는_요청;
import static nextstep.member.acceptance.MemberSteps.깃허브_인증_요청;
import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.utils.DataLoader.ADMIN_EMAIL;
import static nextstep.utils.DataLoader.ADMIN_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private DataLoader dataLoader;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        dataLoader.loadData();
    }

    @DisplayName("올바른 로그인 정보로 로그인 요청시, 인증에 성공한다(토큰 발급)")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(ADMIN_EMAIL, ADMIN_PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("올바르지 않은 비밀번호로 로그인 요청시, 인증에 실패한다")
    @Test
    void wrongPassword() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_실패하는_요청(ADMIN_EMAIL, "틀린 암호");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("올바르지 않은 이메일로 로그인 요청시, 인증에 실패한다")
    @Test
    void notExistEmail() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_실패하는_요청("존재하지 않는 이메일", ADMIN_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("깃허브 권한 증서(code)로 로그인 요청시, 인증에 성공한다(토큰 발급)")
    @Test
    void githubAuth() {
        ExtractableResponse<Response> response = 깃허브_인증_요청(GithubResponses.사용자1.getCode());

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}