package nextstep.subway.acceptance.auth;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.member.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @BeforeEach
    void before() {
        회원_생성_요청(EMAIL, PASSWORD, 20);
    }


    @DisplayName("이메일과 비밀번호로 토큰을 응답받는다.")
    @Test
    void loginTest() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }


    @DisplayName("가입되지 않은 이메일로 로그인을 요청할 경우 토큰을 응답받을 수 없다.")
    @Test
    void loginExceptionTest1() {
        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청("kkk@emgail.com", PASSWORD);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("일치하지 않은 비밀번호로 로그인을 요청할 경우 토큰을 응답받을 수 없다.")
    @Test
    void loginExceptionTest2() {
        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, "kkkkkk");
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("깃허브에 가입된 사용자라면 코드를 이용하여 토큰을 응답받을 수 있다.")
    @EnumSource(GithubResponses.class)
    @ParameterizedTest
    void githubLoginTest(GithubResponses user) {
        // when
        ExtractableResponse<Response> response = 깃허브_인증_로그인_요청(user.getCode());

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("accessToken")).isNotBlank()
        )
        ;
    }

    @DisplayName("깃허브에 가입된 사용자가 아니라면 토큰을 응답받을 수 없다.")
    @Test
    void githubLoginExceptionTest() {
        // when
        ExtractableResponse<Response> response = 깃허브_인증_로그인_요청("kkk111222");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }



}