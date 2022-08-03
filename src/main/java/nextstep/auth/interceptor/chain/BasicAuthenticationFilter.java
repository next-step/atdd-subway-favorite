package nextstep.auth.interceptor.chain;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.user.UserDetailsService;
import nextstep.member.domain.LoginMember;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends ChainInterceptor {
    private UserDetailsService userDetailService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailService = userDetailsService;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        AuthenticationToken token = getToken(authHeader);

        LoginMember loginMember = userDetailService.loadUserByUsername(token.getPrincipal());
        validationLoginMember(token, loginMember);

        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }

    private AuthenticationToken getToken(String authHeader) {
        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }

    private void validationLoginMember(AuthenticationToken token, LoginMember loginMember) {
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
