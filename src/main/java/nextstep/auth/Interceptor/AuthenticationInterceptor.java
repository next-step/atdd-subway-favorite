package nextstep.auth.Interceptor;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.AuthService;
import nextstep.auth.domain.AuthServices;
import nextstep.member.domain.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    public static final String MY_INFO_ATTR_NAME = "myInfo";
    private final AuthServices authServices;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        AuthService authService = authServices.findAuth(header);
        authService.validate(header);
        Member myInfo = authService.findMember(header);
        request.setAttribute(MY_INFO_ATTR_NAME, myInfo);

        return true;
    }
}