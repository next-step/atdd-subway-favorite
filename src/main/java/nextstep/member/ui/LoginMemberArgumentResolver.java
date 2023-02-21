package nextstep.member.ui;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.RoleType;
import nextstep.member.domain.auth.AuthenticationPrincipal;
import nextstep.member.domain.auth.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = resolveToken(webRequest);

        String principal = jwtTokenProvider.getPrincipal(accessToken);
        List<RoleType> roles = jwtTokenProvider.getRoles(accessToken);

        return new LoginMember(principal, roles);
    }

    private String resolveToken(NativeWebRequest webRequest) {
        String authHeader = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            verifyToken(token);
            return token;
        }

        return null;
    }

    private void verifyToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }
    }
}
