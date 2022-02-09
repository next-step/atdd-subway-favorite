package nextstep.auth.unit.authentication;

import nextstep.auth.authentication.*;
import nextstep.auth.context.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class UsernamePasswordAuthenticationProviderTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private AuthenticationProvider provider;
    private AuthenticationToken authenticationToken;

    @BeforeEach
    void setUp() {
        UserDetails expectedUserDetails = mock(UserDetails.class);
        Mockito.when(expectedUserDetails.getUsername()).thenReturn(EMAIL);
        Mockito.when(expectedUserDetails.getPassword()).thenReturn(PASSWORD);
        Mockito.when(expectedUserDetails.checkPassword(any())).thenReturn(true);

        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(expectedUserDetails);

        provider = new UsernamePasswordAuthenticationProvider(userDetailsService);
        authenticationToken = new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD);
    }

    @Test
    void authenticate() {

        Authentication authentication = provider.authenticate(authenticationToken);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        assertThat(userDetails.getUsername()).isEqualTo(EMAIL);
        assertThat(userDetails.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    void supports() {
        assertThat(provider.supports(authenticationToken.getClass())).isTrue();
    }
}