package nextstep.auth.interceptor.nonchain;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;

public class UsernamePasswordAuthenticationFilter extends AuthenticateNonChainInterceptor {

    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    UserDetails createAuthentication(final HttpServletRequest request) {
        try {
            final String username = request.getParameter("username");
            final String password = request.getParameter("password");
            final AuthenticationToken token = new AuthenticationToken(username, password);

            final UserDetails loginMember = userDetailsService.loadUserByUsername(token.getPrincipal());

            nullValidation(loginMember);

            loginMember.checkPassword(token.getCredentials());
            return loginMember;
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }

    @Override
    void afterHandle(final UserDetails loginMember, final HttpServletResponse response) {
        final Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void nullValidation(final UserDetails loginMember) {
        if (Objects.isNull(loginMember)) {
            throw new AuthenticationException();
        }
    }

}
