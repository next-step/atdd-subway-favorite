package nextstep.auth.authentication.filter.checking;

import nextstep.auth.authentication.*;
import nextstep.auth.context.Authentication;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class BasicAuthenticationFilter extends AuthenticationCheckingFilter {
    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) throws IOException {
        var authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        var authHeader = new String(Base64.decodeBase64(authCredentials));

        var splits = authHeader.split(":");
        var principal = splits[0];
        var credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }

    @Override
    protected Authentication authenticate(AuthenticationToken authenticationToken) {
        var userDetails = userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());

        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.getPassword().equals(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(userDetails.getUsername(), userDetails.getAuthorities());
    }
}
