package nextstep.auth.authentication.nonchain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.User;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
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
        AuthenticationToken authenticationToken = convert(request);

        Authentication authenticate = authenticate(authenticationToken);

        SecurityContextMapper.setContext(authenticate.getPrincipal().toString(), authenticate.getAuthorities());
        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) {
        String userEmail = request.getParameter("username");
        String password = request.getParameter("password");
        return new AuthenticationToken(userEmail, password);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        User user = userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());

        if(!user.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(user.getUsername(),user.getAuthorities());
    }
}
