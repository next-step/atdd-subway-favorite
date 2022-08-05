package nextstep.auth.authentication.filter.checking;

import nextstep.auth.authentication.filter.AuthenticationFilter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationCheckingFilter extends AuthenticationFilter {

    @Override
    protected void processing(Authentication authentication, HttpServletResponse response) throws Exception {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean proceed() {
        return true;
    }
}
