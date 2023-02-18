package nextstep.subway.unit;

import nextstep.auth.application.AuthService;
import nextstep.auth.infra.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.config.exception.PasswordMatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static nextstep.member.config.message.MemberError.UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("인증 관련 Mock 테스트")
@ExtendWith(MockitoExtension.class)
class AuthServiceMockTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String TOKEN = "token";
    @Mock
    private MemberService memberService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private AuthService authService;

    @DisplayName("로그인 시 패스워드가 다르다.")
    @Test
    void success_loginTest() {

        when(memberService.findByEmail(anyString())).thenReturn(createMember(EMAIL, PASSWORD, 10));
        when(jwtTokenProvider.createToken(anyString(), anyList())).thenReturn(TOKEN);

        final TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);

        final TokenResponse tokenResponse = authService.login(tokenRequest);

        assertAll(
                () -> assertThat(tokenResponse.getAccessToken()).isNotNull()
        );
    }

    @DisplayName("로그인 시 패스워드가 다르다.")
    @Test
    void error_loginTest() {

        when(memberService.findByEmail(anyString())).thenReturn(createMember(EMAIL, PASSWORD, 10));

        final TokenRequest tokenRequest = new TokenRequest(EMAIL, "different password");
        assertThatThrownBy(() -> authService.login(tokenRequest))
                .isInstanceOf(PasswordMatchException.class)
                .hasMessage(UNAUTHORIZED.getMessage());
    }

    private Member createMember(final String email, final String password, final Integer age) {
        return new Member(email, password, age);
    }
}