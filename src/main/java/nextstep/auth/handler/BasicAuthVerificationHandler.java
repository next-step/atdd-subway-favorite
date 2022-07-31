package nextstep.auth.handler;

import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthVerificationHandler extends AuthVerificationHandler {
    private final UserDetailsService userDetailsService;

    public BasicAuthVerificationHandler(UserDetailsService userDetailsService) {
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
