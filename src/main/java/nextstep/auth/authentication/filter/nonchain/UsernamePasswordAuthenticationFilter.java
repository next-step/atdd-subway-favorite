package nextstep.auth.authentication.filter.nonchain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.authentication.provider.ProviderManager;
import nextstep.auth.authentication.provider.ProviderType;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter extends AuthenticationNonChainFilter {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final ProviderManager providerManager;

    @Override
    protected AuthenticationToken createToken(HttpServletRequest request) {
        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);

        return new AuthenticationToken(username, password);
    }

    @Override
    protected Authentication authenticate(AuthenticationToken token) {
        AuthenticationProvider authenticationProvider =  providerManager.getAuthenticationProvider(ProviderType.USER_PASSWORD);
        return authenticationProvider.authenticate(token);
    }

    @Override
    protected boolean send(Authentication authentication, HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return false;
    }
}
