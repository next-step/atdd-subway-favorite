package nextstep.subway.auth.ui.authentication;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationInterceptorTest {

    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private CustomUserDetailsService userDetailsService;
    private AuthenticationToken authenticationToken;

    @BeforeEach
    void setUp(){
        userDetailsService = mock(CustomUserDetailsService.class);
        authenticationToken = mock(AuthenticationToken.class);

        when(authenticationToken.getPrincipal()).thenReturn(EMAIL);
        when(authenticationToken.getCredentials()).thenReturn(PASSWORD);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
    }

    @DisplayName("세션기반 인증")
    @Test
    void authenticateSession(){
        // given
        AuthenticationInterceptor authenticationInterceptor = new SessionAuthenticationInterceptor();

        // when
        Authentication authentication = authenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(((LoginMember) authentication.getPrincipal()).getEmail()).isEqualTo(EMAIL);
        assertThat(((LoginMember) authentication.getPrincipal()).getPassword()).isEqualTo(PASSWORD);
    }

    @DisplayName("토큰기반 인증")
    @Test
    void authenticateToken(){
        // given
        AuthenticationInterceptor authenticationInterceptor = new TokenAuthenticationInterceptor();

        // when
        Authentication authentication = authenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(((LoginMember) authentication.getPrincipal()).getEmail()).isEqualTo(EMAIL);
        assertThat(((LoginMember) authentication.getPrincipal()).getPassword()).isEqualTo(PASSWORD);
    }
}