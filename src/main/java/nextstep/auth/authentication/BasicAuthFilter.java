package nextstep.auth.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.user.UserDetail;
import nextstep.auth.user.UserDetailService;
import nextstep.auth.interceptor.AuthContextChainInterceptor;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
public class BasicAuthFilter extends AuthContextChainInterceptor {
    private final UserDetailService userDetailService;

    @Override
    protected void checkValidAuth(final AuthenticationToken token) {
        UserDetail loginMember = userDetailService.loadUserByUsername(token.getPrincipal());
        if (loginMember == null) {
            throw new AuthenticationException();
        }
        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }

    @Override
    protected Authentication getAuthentication(final AuthenticationToken token) {
        UserDetail loginMember = userDetailService.loadUserByUsername(token.getPrincipal());
        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }

    @Override
    protected AuthenticationToken createAuthToken(final HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }
}
