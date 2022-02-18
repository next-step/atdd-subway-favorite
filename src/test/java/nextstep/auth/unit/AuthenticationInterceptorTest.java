package nextstep.auth.unit;

import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import org.junit.jupiter.api.Test;

import static nextstep.auth.unit.AuthData.EMAIL;
import static nextstep.auth.unit.AuthData.PASSWORD;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AuthenticationInterceptorTest extends AuthTest {
    private AuthenticationInterceptor authenticationInterceptor;

    @Test
    void authenticate() {
        // given
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);
        authenticationInterceptor = new EmptyAuthenticationInterceptor(userDetailsService);

        // when & then
        assertDoesNotThrow(() -> authenticationInterceptor.authenticate(authenticationToken));
    }
}
