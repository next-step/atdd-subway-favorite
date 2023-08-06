package nextstep.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.token.oauth2.github.GithubTokenRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.auth.acceptance.AuthSteps.깃허브_로그인요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("로그인 요청 시 로그인에 성공하면 토큰을 응답한다.")
    @Test
    void 로그인요청() {
        // given
        회원저장();

        // when
        ExtractableResponse<Response> response = AuthSteps.로그인요청(EMAIL, PASSWORD);

        // then
        로그인요청_응답값_검증(response);
    }

    @DisplayName("로그인 요청 시 패스워드 검증에 실패하면 실패응답한다.")
    @Test
    void 로그인요청_패스워드검증실패() {
        // given
        회원저장();

        // when
        ExtractableResponse<Response> response = AuthSteps.로그인요청(EMAIL, "");

        // then
        로그인요청_패스워드검증실패_응답값_검증(response);
    }

    @DisplayName("로그인 요청 시 패스워드 검증에 실패하면 실패응답한다.")
    @Test
    void 로그인요청_사용자미존재() {
        // given
        회원저장();

        // when
        ExtractableResponse<Response> response = AuthSteps.로그인요청("", "");

        // then
        로그인요청_사용자미존재_응답값_검증(response);
    }

    @DisplayName("깃허브 로그인 요청 시 로그인에 성공하면 토큰을 응답한다.")
    @Test
    void 깃허브로그인요청() {
        // when
        ExtractableResponse<Response> response = 깃허브_로그인요청("code1");

        // then
        깃허브로그인요청_응답값_검증(response);
    }

    @DisplayName("깃허브 로그인 요청 시 유효하지 않은 코드로 요청하면 실패를 응답한다.")
    @Test
    void 깃허브로그인요청_코드미존재() {
        // when
        ExtractableResponse<Response> response = 깃허브_로그인요청("code");

        // then
        깃허브로그인요청_코드미존재_응답값_검증(response);
    }

    private void 회원저장() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
    }

    private void 로그인요청_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getObject("accessToken", String.class)).isNotBlank();
    }

    private void 로그인요청_패스워드검증실패_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.asString()).isEqualTo("인증에 실패했습니다.");
    }

    private void 로그인요청_사용자미존재_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.asString()).isEqualTo("인증에 실패했습니다.");
    }

    private void 깃허브로그인요청_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getObject("accessToken", String.class)).isNotBlank();
    }

    private void 깃허브로그인요청_코드미존재_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}