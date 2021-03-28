package nextstep.subway.auth.ui.persistence;

import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class SecurityContextInterceptor implements HandlerInterceptor {

    /*
    * preHandle
    * SecurityContextHolder에 인증정보 담기
    * */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityContextHolder.clearContext();
    }
}
