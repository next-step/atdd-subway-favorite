package nextstep.subway.auth.ui.authentication;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.converter.AuthenticationConverter;
import nextstep.subway.auth.ui.converter.SessionAuthenticationConverter;
import nextstep.subway.auth.ui.converter.TokenAuthenticationConverter;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private CustomUserDetailsService userDetailsService;
    private AuthenticationToken authenticationToken;
    private AuthenticationConverter authenticationConverter;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp(){
        userDetailsService = mock(CustomUserDetailsService.class);
        authenticationToken = mock(AuthenticationToken.class);
        jwtTokenProvider = new JwtTokenProvider();

        when(authenticationToken.getPrincipal()).thenReturn(EMAIL);
        when(authenticationToken.getCredentials()).thenReturn(PASSWORD);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
    }

    @DisplayName("세션기반 인증")
    @Test
    void authenticateSession(){
        // given
        authenticationConverter = mock(SessionAuthenticationConverter.class);
        AuthenticationInterceptor authenticationInterceptor = new SessionAuthenticationInterceptor(authenticationConverter, userDetailsService);

        // when
        Authentication authentication = authenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(((LoginMember) authentication.getPrincipal()).getEmail()).isEqualTo(EMAIL);
        assertThat(((LoginMember) authentication.getPrincipal()).getPassword()).isEqualTo(PASSWORD);
    }

    @DisplayName("[예외처리] 세션기반 인증 - 비밀번호 오류")
    @Test
    void authenticateSessionWithWrongInfo(){
        // given
        authenticationConverter = mock(SessionAuthenticationConverter.class);
        AuthenticationInterceptor authenticationInterceptor = new SessionAuthenticationInterceptor(authenticationConverter, userDetailsService);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD + "111", 20));

        // when + then
        assertThatThrownBy(() -> {
            authenticationInterceptor.authenticate(authenticationToken);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("토큰기반 인증")
    @Test
    void authenticateToken(){
        // given
        authenticationConverter = mock(TokenAuthenticationConverter.class);
        AuthenticationInterceptor authenticationInterceptor = new TokenAuthenticationInterceptor(authenticationConverter, userDetailsService, jwtTokenProvider);

        // when
        Authentication authentication = authenticationInterceptor.authenticate(authenticationToken);

        // then
        assertThat(((LoginMember) authentication.getPrincipal()).getEmail()).isEqualTo(EMAIL);
        assertThat(((LoginMember) authentication.getPrincipal()).getPassword()).isEqualTo(PASSWORD);
    }

    @DisplayName("[예외처리] 토큰기반 인증 - 비밀번호 오류")
    @Test
    void authenticateTokenWithWrongInfo(){
        // given
        authenticationConverter = mock(TokenAuthenticationConverter.class);
        AuthenticationInterceptor authenticationInterceptor = new TokenAuthenticationInterceptor(authenticationConverter, userDetailsService, jwtTokenProvider);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD + "111", 20));

        // when + then
        assertThatThrownBy(() -> {
            authenticationInterceptor.authenticate(authenticationToken);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}