package nextstep.subway.config;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.message.Message;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class FavoriteInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("authorization");
        if (token == null) {
            throw new AuthenticationException(Message.INVALID_TOKEN.getMessage());
        }
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException(Message.INVALID_TOKEN.getMessage());
        }
        String email = jwtTokenProvider.getPrincipal(token);
        boolean isEmailPresent = memberRepository.findByEmail(email).isPresent();
        if (!isEmailPresent) {
            throw new AuthenticationException(Message.INVALID_TOKEN.getMessage());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
