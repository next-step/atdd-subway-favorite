package nextstep.auth.acceptance;

import nextstep.global.error.code.ErrorCode;
import nextstep.support.AcceptanceTest;
import nextstep.support.AssertUtils;
import nextstep.support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static nextstep.auth.acceptance.step.AuthStep.깃허브_로그인_요청;
import static nextstep.auth.acceptance.step.AuthStep.일반_로그인_요청;
import static nextstep.auth.acceptance.utils.GithubMockUser.김민지;
import static nextstep.member.acceptance.step.MemberStep.회원_생성_요청;
import static nextstep.member.fixture.MemberFixture.회원_정보_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AcceptanceTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanUp;

    private static final String ACCESS_TOKEN_KEY = "accessToken";

    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();

        회원_생성_요청(회원_정보_DTO);
    }

    /**
     * When 일반 로그인을 하면
     * Then 토큰을 발급 받는다
     */
    @DisplayName("일반 로그인을 하면 토큰을 발급 받는다")
    @Test
    void successBearerLogin() {
        // when
        var 일반_로그인_응답 = 일반_로그인_요청(회원_정보_DTO.getEmail(), 회원_정보_DTO.getPassword());

        // then
        assertThat(일반_로그인_응답.jsonPath().getString(ACCESS_TOKEN_KEY)).isNotBlank();
    }

    /**
     * When 존재하지 않은 회원으로 로그인을 요청하면
     * Then 토큰 발행에 실패한다.
     */
    @DisplayName("존재하지 않은 회원으로 로그인을 요청하면 토큰 발행에 실패한다.")
    @Test
    void failedBearerLoginWhenAttemptNotExistMember() {
        // when
        var 일반_로그인_응답 = 일반_로그인_요청("not_exist@test.com", 회원_정보_DTO.getPassword());

        // then
        assertAll(
                () -> AssertUtils.assertThatStatusCode(일반_로그인_응답, HttpStatus.UNAUTHORIZED),
                () -> AssertUtils.assertThatErrorMessage(일반_로그인_응답, ErrorCode.LOGIN_ERROR),
                () -> assertThat(일반_로그인_응답.jsonPath().getString(ACCESS_TOKEN_KEY)).isBlank()
        );
    }

    /**
     * When 틀린 비밀번호로 로그인을 요청하면
     * Then 토큰 발행에 실패한다.
     */
    @DisplayName("틀린 비밀번호로 로그인을 요청하면 토큰 발행에 실패한다.")
    @Test
    void failedBearerLoginWhenAttemptWrongPassword() {
        // when
        var 일반_로그인_응답 = 일반_로그인_요청(회원_정보_DTO.getEmail(), "wrong_password");

        // then
        assertAll(
                () -> AssertUtils.assertThatStatusCode(일반_로그인_응답, HttpStatus.UNAUTHORIZED),
                () -> AssertUtils.assertThatErrorMessage(일반_로그인_응답, ErrorCode.LOGIN_ERROR),
                () -> assertThat(일반_로그인_응답.jsonPath().getString(ACCESS_TOKEN_KEY)).isBlank()
        );
    }

    /**
     * When 깃허브 로그인을 하면
     * Then 토큰을 발급 받는다
     */
    @DisplayName("깃허브 로그인을 하면 토큰을 발급 받는다")
    @Test
    void successGithubLogin() {
        // when
        var 깃허브_토큰_응답 = 깃허브_로그인_요청(김민지.getCode());

        // then
        assertThat(깃허브_토큰_응답.jsonPath().getString(ACCESS_TOKEN_KEY)).isNotBlank();
    }

    /**
     * When 올바르지 않은 코드로 깃허브 로그인을 요청하면
     * Then 토큰을 발급 받지 못한다.
     */
    @DisplayName("올바르지 않은 코드로 깃허브 로그인을 요청하면 토큰 발행에 실패한다.")
    @Test
    void failedGithubLoginWhenAttemptWrongCode() {
        // when
        var 깃허브_토큰_응답 = 깃허브_로그인_요청("wrong_code");

        // then
        assertAll(
                () -> AssertUtils.assertThatStatusCode(깃허브_토큰_응답, HttpStatus.UNAUTHORIZED),
                () -> assertThat(깃허브_토큰_응답.jsonPath().getString(ACCESS_TOKEN_KEY)).isBlank()
        );
    }
}