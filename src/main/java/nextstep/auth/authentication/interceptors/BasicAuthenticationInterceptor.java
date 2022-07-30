package nextstep.auth.authentication.interceptors;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.AuthenticateRequest;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationInterceptor extends ChainingAuthenticationInterceptor {
    private final LoginMemberService loginMemberService;

    public BasicAuthenticationInterceptor(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    AuthenticateRequest createLoginRequest(final HttpServletRequest request) {
        final String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        final String authHeader = new String(Base64.decodeBase64(authCredentials));

        final String[] splits = authHeader.split(":");
        final String principal = splits[0];
        final String credentials = splits[1];

        return new AuthenticateRequest(principal, credentials);
    }

    @Override
    LoginMember findLoginMember(final AuthenticateRequest request) {
        final LoginMember loginMember = loginMemberService.loadUserByUsername(request.getEmail());
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        return loginMember;
    }

    @Override
    boolean isAuthenticated(final AuthenticateRequest authenticateRequest, final LoginMember loginMember) {
        return loginMember.checkPassword(authenticateRequest.getPassword());
    }

}
