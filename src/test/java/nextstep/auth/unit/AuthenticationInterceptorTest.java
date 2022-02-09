package nextstep.auth.unit;

import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AuthenticationInterceptorTest extends AuthTest {
    private AuthenticationInterceptor authenticationInterceptor;

    @Test
    void authenticate() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        authenticationInterceptor = new EmptyAuthenticationInterceptor(customUserDetailsService);

        // when & then
        assertDoesNotThrow(() -> authenticationInterceptor.authenticate(authenticationToken));
    }
}
