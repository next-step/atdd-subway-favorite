package nextstep.subway.unit.auth.authentication;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.auth.application.UserDetailsService;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.domain.UserDetails;
import nextstep.subway.acceptance.auth.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private AuthenticationInterceptor authenticationInterceptor;

    @Test
    void authenticate() {
        // given
        Long ID = 1L;
        String EMAIL = "email@email.com";
        String PASSWORD = "password";
        int AGE = 10;
        UserDetails userDetails = new UserDetailsImpl(1L, EMAIL, PASSWORD);
        when(userDetailsService.loadUserByUsername(EMAIL))
            .thenReturn(userDetails);

        // when
        Authentication authentication = authenticationInterceptor.authenticate(new AuthenticationToken(EMAIL, PASSWORD));
        UserDetails returnLoginMember = (UserDetails) authentication.getPrincipal();
        // then
        assertThat(returnLoginMember.getId()).isEqualTo(ID);
        assertThat(returnLoginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(returnLoginMember.getPassword()).isEqualTo(PASSWORD);
    }
}
