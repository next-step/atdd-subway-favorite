package nextstep.auth.ui;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.TokenManager;
import nextstep.auth.application.TokenType;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final TokenManager tokenManager;

    public AuthenticationPrincipalArgumentResolver(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");
        if ((authorization == null)) {
            throw new AuthenticationException("로그인이 필요합니다.");
        }

        TokenType tokenType = TokenType.findBy(authorization.split(" ")[0]);

        String token = authorization.split(" ")[1];
        String email = tokenManager.getPrincipal(token, tokenType);

        return new LoginMember(email);
    }
}
