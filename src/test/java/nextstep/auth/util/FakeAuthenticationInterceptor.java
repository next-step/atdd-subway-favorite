package nextstep.auth.util;

import nextstep.auth.UserDetailsService;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.auth.util.authFixture.DEFAULT_EMAIL;
import static nextstep.auth.util.authFixture.DEFAULT_PASSWORD;

public class FakeAuthenticationInterceptor extends AuthenticationInterceptor {

    public FakeAuthenticationInterceptor(final UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public AuthenticationToken convert(final HttpServletRequest request) throws IOException {
        return new AuthenticationToken(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }

    @Override
    protected void afterAuthentication(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        return;
    }
}
