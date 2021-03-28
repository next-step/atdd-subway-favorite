package nextstep.subway.auth.ui.authentication;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor{

    /*
     * preHandle
     *  - convert : request -> AuthenticationToken
     *  - authenticate : AuthenticationToken -> Authentication
     *  - afterAuthentication : 세션) 세션에 인증토큰 담기 / 토큰) 응답에 인증토큰 담기
     * */
    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        return null;
    }
}
