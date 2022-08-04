package nextstep.auth.authentication.chain;

import nextstep.auth.CustomUserDetails;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.SecurityContextMapper;
import nextstep.auth.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BasicAuthenticationFilter implements AuthenticationChainFilter {
    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
            String authHeader = new String(Base64.decodeBase64(authCredentials));

            String[] splits = authHeader.split(":");
            String principal = splits[0];
            String credentials = splits[1];

            AuthenticationToken token = new AuthenticationToken(principal, credentials);

            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());

            if (!userDetails.checkPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }

            SecurityContextMapper.setContext(userDetails.getUsername(), userDetails.getAuthorities());
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
