package nextstep.subway.unit;

import nextstep.auth.application.AuthService;
import nextstep.auth.application.dto.*;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.config.exception.PasswordMatchException;
import nextstep.subway.acceptance.ApplicationContextTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.member.config.message.MemberError.UNAUTHORIZED;
import static nextstep.subway.utils.GithubResponses.USER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("인증 관련 테스트")
@Transactional
class AuthServiceTest extends ApplicationContextTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        memberRepository.save(createMember(USER1.getEmail(), PASSWORD, 10));
    }

    @DisplayName("로그인 성공한다.")
    @Test
    void success_loginTest() {
        final TokenRequest tokenRequest = new TokenRequest(USER1.getEmail(), PASSWORD);

        final TokenResponse tokenResponse = authService.login(tokenRequest);
        assertAll(
                () -> assertThat(tokenResponse.getAccessToken()).isNotNull()
        );
    }

    @DisplayName("로그인 시 패스워드가 다르다.")
    @Test
    void error_loginTest() {
        final TokenRequest tokenRequest = new TokenRequest(USER1.getEmail(), "different password");
        assertThatThrownBy(() -> authService.login(tokenRequest))
                .isInstanceOf(PasswordMatchException.class)
                .hasMessage(UNAUTHORIZED.getMessage());
    }

    @DisplayName("Github 로그인에 성공한다.")
    @Test
    void success_githubLoginTest() {
        final GithubTokenRequest githubTokenRequest = new GithubTokenRequest(USER1.getCode());

        final GithubAccessTokenResponse response = authService.loginByGithub(githubTokenRequest);
        assertAll(
                () -> assertThat(response.getAccessToken()).isNotNull()
        );
    }

    private Member createMember(final String email, final String password, final Integer age) {
        return new Member(email, password, age);
    }
}