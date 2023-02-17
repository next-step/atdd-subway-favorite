package nextstep.subway.unit;

import nextstep.auth.application.AuthService;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.config.exception.PasswordMatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.member.config.message.MemberError.UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("인증 관련 테스트")
@SpringBootTest
@Transactional
class AuthServiceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthService authService;

    @DisplayName("로그인 성공한다.")
    @Test
    void success_loginTest() {

        memberRepository.save(createMember(EMAIL, PASSWORD, 10));
        final TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);

        final TokenResponse tokenResponse = authService.login(tokenRequest);

        assertAll(
                () -> assertThat(tokenResponse.getAccessToken()).isNotNull()
        );
    }

    @DisplayName("로그인 시 패스워드가 다르다.")
    @Test
    void error_loginTest() {

        memberRepository.save(createMember(EMAIL, PASSWORD, 10));
        final TokenRequest tokenRequest = new TokenRequest(EMAIL, "different password");

        assertThatThrownBy(() -> authService.login(tokenRequest))
                .isInstanceOf(PasswordMatchException.class)
                .hasMessage(UNAUTHORIZED.getMessage());
    }

    private Member createMember(final String email, final String password, final Integer age) {
        return new Member(email, password, age);
    }
}