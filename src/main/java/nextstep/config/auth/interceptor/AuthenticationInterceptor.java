package nextstep.config.auth.interceptor;

import nextstep.common.exception.InvalidTokenException;
import nextstep.config.auth.AuthenticationContextHolder;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    public static final String AUTHENTICATION_TYPE = "Bearer";
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthenticationInterceptor(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String principal = AuthenticationContextHolder.getAuthentication(null);

        if (ObjectUtils.isEmpty(principal)) {
            String accessToken = extractCredentialsFromAuthorization(request);
            memberService.findMemberByEmail(jwtTokenProvider.getPrincipal(accessToken));

            AuthenticationContextHolder.setAuthentication(accessToken);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthenticationContextHolder.clearContext();
    }

    private void validateAuthorization(String accessToken) {
        if (Objects.isNull(accessToken)) {
            throw new InvalidTokenException("UnAuthorized accessToken type.");
        }

        if (!accessToken.startsWith(AUTHENTICATION_TYPE)) {
            throw new InvalidTokenException("UnAuthorized accessToken type.");
        }
    }

    private String extractCredentialsFromAuthorization(HttpServletRequest request) {
        String value = request.getHeader(HttpHeaders.AUTHORIZATION);

        validateAuthorization(value);

        String accessToken = value.replace(String.format("%s ", AUTHENTICATION_TYPE), "");

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new InvalidTokenException(String.format("%s is UnAuthorized token", value));
        }

        return accessToken;
    }
}
