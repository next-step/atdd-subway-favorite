package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.application.PasswordCheckableUserService;
import nextstep.member.domain.PasswordCheckableUser;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final PasswordCheckableUserService customUserDetailsService;

    public AuthenticationInterceptor(PasswordCheckableUserService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        PasswordCheckableUser passwordCheckableUser = customUserDetailsService.loadUserByUsername(principal);
        checkAuthentication(passwordCheckableUser, token);

        return new Authentication(passwordCheckableUser);
    }

    protected void checkAuthentication(PasswordCheckableUser passwordCheckableUser, AuthenticationToken token) {
        if (passwordCheckableUser == null) {
            throw new AuthenticationException();
        }

        if (!passwordCheckableUser.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
