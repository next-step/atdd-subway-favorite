package nextstep.auth.authentication;

import nextstep.auth.token.LoginRequest;
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
    LoginRequest createLoginRequest(final HttpServletRequest request) {
        final String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        final String authHeader = new String(Base64.decodeBase64(authCredentials));

        final String[] splits = authHeader.split(":");
        final String principal = splits[0];
        final String credentials = splits[1];

        return new LoginRequest(principal, credentials);
    }

    @Override
    LoginMember findLoginMember(final LoginRequest request) {
        final LoginMember loginMember = loginMemberService.loadUserByUsername(request.getEmail());
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        return loginMember;
    }

    @Override
    boolean isAuthenticated(final LoginRequest loginRequest, final LoginMember loginMember) {
        return loginMember.checkPassword(loginRequest.getPassword());
    }

}
