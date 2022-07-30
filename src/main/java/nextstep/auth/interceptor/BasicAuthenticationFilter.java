package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.userdetails.UserDetails;
import nextstep.member.application.UserDetailsService;
import nextstep.member.domain.NotFoundMemberException;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends AuthenticationChainingFilter {
    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        AuthenticationToken token = createAuthenticationToken(request);

        try {
            UserDetails userDetails = findUser(token);
            return new Authentication(userDetails.getPrincipal(), userDetails.getAuthorities());
        } catch (NotFoundMemberException e) {
            throw new AuthenticationException();
        }
    }

    private UserDetails findUser(AuthenticationToken token) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());
            if (userDetails.invalidCredentials(token.getCredentials())) {
                throw new AuthenticationException();
            }
            return userDetails;
        } catch (NotFoundMemberException e) {
            throw new AuthenticationException();
        }
    }

    private AuthenticationToken createAuthenticationToken(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];
        return new AuthenticationToken(principal, credentials);
    }


}
