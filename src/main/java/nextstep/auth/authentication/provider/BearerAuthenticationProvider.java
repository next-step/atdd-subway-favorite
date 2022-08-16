package nextstep.auth.authentication.provider;

import nextstep.auth.authentication.execption.AuthenticationException;
import nextstep.auth.authentication.token.BasicAuthenticationToken;
import nextstep.auth.authentication.token.BearerAuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;

import java.util.List;

public class BearerAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerAuthenticationProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String credentials = (String) authentication.getCredentials();
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(credentials);
        List<String> roles = jwtTokenProvider.getRoles(credentials);

        return new BearerAuthenticationToken(principal, roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BasicAuthenticationToken.class);
    }
}