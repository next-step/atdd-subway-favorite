package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.exception.InvalidAuthenticationTokenException;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationProviderTest {

    private static final Long ID = 1L;
    private static final String EMAIL = "dhlee@test.com";
    private static final String PASSWORD = "Pas5W0rb12$%";
    private static final Integer AGE = 10;
    private AuthenticationProvider authenticationProvider;

    @BeforeEach
    void setUp() {
        UserDetailsService userDetailService = mock(UserDetailsService.class);
        when(userDetailService.loadUserByUsername(anyString())).thenReturn(new LoginMember(ID, EMAIL, PASSWORD, AGE));
        authenticationProvider = new AuthenticationProvider(userDetailService);
    }

    @Test
    @DisplayName("정상적으로 인증된 사용자의 인증정보를 받아온다.")
    public void authenticateTest() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        // when
        Authentication authentication = authenticationProvider.authenticate(authenticationToken);

        // then
        assertThat(authentication).isNotNull();
        Object principal = authentication.getPrincipal();
        assertThat(principal).isInstanceOf(LoginMember.class);

        LoginMember loginMember = (LoginMember) principal;
        assertThat(loginMember.getId()).isEqualTo(ID);
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(loginMember.getAge()).isEqualTo(AGE);
    }

    @Test
    @DisplayName("인증정보가 틀린 사용자인 경우 오류를 던진다.")
    public void authenticateFailTest() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, "invalid password");

        // when
        assertThatThrownBy(() -> authenticationProvider.authenticate(authenticationToken))
                .isInstanceOf(InvalidAuthenticationTokenException.class);
    }

    @Test
    @DisplayName("잘못된 principal로 인증하는 경우 오류를 던진다.")
    public void authenticateFailTest2() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken("unknown user", "invalid password");

        // when
        assertThatThrownBy(() -> authenticationProvider.authenticate(authenticationToken))
                .isInstanceOf(InvalidAuthenticationTokenException.class);
    }
}