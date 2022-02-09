package nextstep.auth.unit.authentication;

import nextstep.auth.authentication.AuthenticationProvider;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.UsernamePasswordAuthenticationProvider;
import nextstep.auth.authentication.UsernamePasswordAuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UsernamePasswordAuthenticationProviderTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private AuthenticationProvider provider;
    private AuthenticationToken authenticationToken;

    @BeforeEach
    void setUp() {
        CustomUserDetailsService customUserDetailsService = mock(CustomUserDetailsService.class);

        LoginMember expectedMember = new LoginMember(-1L, EMAIL, PASSWORD, 0);
        Mockito.when(customUserDetailsService.loadUserByUsername(EMAIL)).thenReturn(expectedMember);
        provider = new UsernamePasswordAuthenticationProvider(customUserDetailsService);

        authenticationToken = new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD);
    }

    @Test
    void authenticate() {

        Authentication authentication = provider.authenticate(authenticationToken);

        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(loginMember.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void supports() {
        assertThat(provider.supports(authenticationToken.getClass())).isTrue();
    }
}