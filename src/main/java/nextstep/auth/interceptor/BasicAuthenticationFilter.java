package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.AuthenticationException;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.NotFoundMemberException;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends AuthenticationChainingFilter {
    private final LoginMemberService loginMemberService;

    public BasicAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        AuthenticationToken token = createAuthenticationToken(request);

        try {
            LoginMember loginMember = findLoginMember(token);
            return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        } catch (NotFoundMemberException e) {
            throw new AuthenticationException();
        }
    }

    private LoginMember findLoginMember(AuthenticationToken token) {
        try {
            LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());
            if (loginMember.invalidPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }
            return loginMember;
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
