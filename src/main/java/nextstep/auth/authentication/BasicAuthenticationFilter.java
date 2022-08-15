package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.interceptor.ChainFilter;
import nextstep.member.user.User;
import nextstep.member.user.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends ChainFilter {

    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication createAuthentication(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        AuthenticationToken token = new AuthenticationToken(principal, credentials);

        User user = userDetailsService.loadUserByUsername(token.getPrincipal());
        if (user == null) {
            throw new AuthenticationException();
        }

        if (!user.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(user.getPrincipal(), user.getAuthorities());
    }

}
