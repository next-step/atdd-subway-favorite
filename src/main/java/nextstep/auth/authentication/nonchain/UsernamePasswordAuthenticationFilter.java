package nextstep.auth.authentication.nonchain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.context.SecurityContextMapper;
import nextstep.user.UserDetails;
import nextstep.user.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private final UserDetailsService userDetailsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String userEmail = request.getParameter("username");
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        SecurityContextMapper.setContext(userDetails.getUsername(), userDetails.getAuthorities());
        return false;
    }
}
