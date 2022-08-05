package nextstep.auth.authentication.filter.checking;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.member.application.LoginMemberService;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class BasicAuthenticationFilter extends AuthenticationCheckingFilter {
    private final LoginMemberService loginMemberService;

    public BasicAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) throws IOException {
        var authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        var authHeader = new String(Base64.decodeBase64(authCredentials));

        var splits = authHeader.split(":");
        var principal = splits[0];
        var credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }

    @Override
    protected Authentication authenticate(AuthenticationToken authenticationToken) {
        var loginMember = loginMemberService.loadUserByUsername(authenticationToken.getPrincipal());

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }
}
