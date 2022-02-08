package nextstep.subway.unit.auth.authentication;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import nextstep.auth.application.UserDetailsService;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.after.AfterAuthentication;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.domain.UserDetails;
import nextstep.subway.acceptance.auth.UserDetailsImpl;
import nextstep.subway.unit.auth.authentication.after.FakeAfterAuthentication;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private AuthenticationConverter authenticationConverter;

    @Test
    void preHandle() throws IOException {
        // given
        FakeAfterAuthentication fakeAfterAuthentication = new FakeAfterAuthentication();
        AuthenticationInterceptor authenticationInterceptor = createAuthenticationInterceptor(fakeAfterAuthentication);

        when(authenticationConverter.convert(any()))
            .thenReturn(new AuthenticationToken(EMAIL, PASSWORD));

        UserDetails userDetails = new UserDetailsImpl(1L, EMAIL, PASSWORD);
        when(userDetailsService.loadUserByUsername(EMAIL))
            .thenReturn(userDetails);

        // when
        authenticationInterceptor.preHandle(new MockHttpServletRequest(), new MockHttpServletResponse(), new Object());
        Authentication returnedAuthentication = fakeAfterAuthentication.getAuthentication();
        UserDetails returnLoginMember = (UserDetails) returnedAuthentication.getPrincipal();

        // then
        assertThat(returnLoginMember.getId()).isEqualTo(ID);
        assertThat(returnLoginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(returnLoginMember.getPassword()).isEqualTo(PASSWORD);
    }

    private AuthenticationInterceptor createAuthenticationInterceptor(AfterAuthentication afterAuthentication) {
        return new AuthenticationInterceptor(
            userDetailsService, authenticationConverter, afterAuthentication
        );
    }
}
