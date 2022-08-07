package nextstep.auth.authentication.nonchain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.User;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.SecurityContextMapper;
import nextstep.auth.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private final UserDetailsService userDetailsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String userEmail = request.getParameter("username");
        String password = request.getParameter("password");
        User userDetails = userDetailsService.loadUserByUsername(userEmail);

        if(!userDetails.checkPassword(password)) {
            throw new AuthenticationException();
        }

        SecurityContextMapper.setContext(userDetails.getUsername(), userDetails.getAuthorities());
        return false;
    }

}
