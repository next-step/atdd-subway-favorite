package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationFilter extends Authorizator {

    private final LoginMemberService loginMemberService;

    public BasicAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        AuthenticationToken token = getToken(request);
        LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());

        checkAuthentication(token, loginMember);

        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }


    private AuthenticationToken getToken(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }

    private void checkAuthentication(AuthenticationToken token, LoginMember loginMember) {
        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
