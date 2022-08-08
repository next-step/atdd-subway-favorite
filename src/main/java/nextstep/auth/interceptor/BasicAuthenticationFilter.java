package nextstep.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;

@RequiredArgsConstructor
public class BasicAuthenticationFilter extends AuthenticationChainHandler {
    private static final String DELIMETER = ":";

    private final UserDetailsService userDetailsService;

    @Override
    public String extractCredentials(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
    }

    @Override
    public UserDetails getUserDetails(String authCredentials) {
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(DELIMETER);
        AuthenticationToken token = new AuthenticationToken(splits[0], splits[1]);

        return userDetailsService.loadUserByUsername(token.getPrincipal());
    }

    @Override
    public Authentication createAuthentication(UserDetails userDetails) {
        return new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
    }
}
