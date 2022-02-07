package nextstep.auth.authentication.after;

import static nextstep.auth.context.SecurityContextHolder.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;

public class SessionAfterAuthentication implements AfterAuthentication {
    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
