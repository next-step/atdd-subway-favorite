package nextstep.auth.interceptor.chain;

import javax.servlet.http.HttpServletRequest;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthenticationFilter extends AuthChainInterceptor {

    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected Authentication createAuthentication(final HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        AuthenticationToken token = new AuthenticationToken(principal, credentials);

        UserDetails loginMember = userDetailsService.loadUserByUsername(token.getPrincipal());
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }

}
