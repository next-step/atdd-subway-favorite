package nextstep.auth.authentication.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.token.UsernamePasswordAuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

// 전송이오면 필터로 가게 된다.
public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    private final AuthenticationManager authenticationManager;

    // 인증 필터는 인증 매니저를 주입 받는다.
    public UsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // 인증 시도를 한다.
    private Authentication attemptAuthentication(HttpServletRequest request) {
        // request 에서 아이디와 이름을 꺼낸뒤
        String username = request.getParameter(USERNAME_FIELD);
        String password = request.getParameter(PASSWORD_FIELD);

        // 인증 토큰을 발급한다.
        Authentication token = new UsernamePasswordAuthenticationToken(username, password);

        // 생성한 토큰 (Authentication 인증 객체)을 인증 매니저에게 전달한다.
        // => (AuthenticationManager 인증 매니저)는 여러개의 AuthenicationProvider 에게 위임.
        // => AuthenicationProvider 는 provider들을 여러개 가지고 있음
        return authenticationManager.authenticate(token);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (checkAlreadyAuthentication()) {
            return true;
        }

        try {
            Authentication authentication = attemptAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    private boolean checkAlreadyAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null;
    }
}
