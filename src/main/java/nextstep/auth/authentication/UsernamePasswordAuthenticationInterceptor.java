package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.UserDetailService;
import nextstep.auth.user.UserDetails;

public class UsernamePasswordAuthenticationInterceptor extends AuthenticationNonChainInterceptor {
    public UsernamePasswordAuthenticationInterceptor(UserDetailService userDetailService) {
        super(userDetailService);
    }

    @Override
    protected UserInformation createPrincipal(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        return new UserInformation(email, password);
    }

    @Override
    protected void afterAuthentication(HttpServletResponse response, UserDetails userDetails) {
        Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
