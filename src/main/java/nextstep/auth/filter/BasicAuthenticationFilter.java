package nextstep.auth.filter;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.userdetail.UserDetailService;
import nextstep.auth.userdetail.UserDetails;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class BasicAuthenticationFilter extends AuthenticationFilter {

    private static final String DELIMITER = ":";

    private final UserDetailService userDetailService;

    @Override
    public Authentication getAuth(HttpServletRequest request) {
        UserDetails user = getUser(getToken(request));
        return new Authentication(user.getPrincipal(), user.getAuthorities());
    }

    private UserDetails getUser(AuthenticationToken token) {
        UserDetails userDetails = getUserDetails(token);
        if (userDetails.isInvalidCredentials(token.getCredentials())) {
            throw new AuthenticationException();
        }
        return userDetails;
    }

    private UserDetails getUserDetails(AuthenticationToken token) {
        try {
            return userDetailService.loadUserByUsername(token.getPrincipal());
        } catch (NoSuchElementException e) {
            throw new AuthenticationException();
        }
    }

    private AuthenticationToken getToken(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] split = authHeader.split(DELIMITER);
        String principal = split[0];
        String credentials = split[1];
        return new AuthenticationToken(principal, credentials);
    }
}
