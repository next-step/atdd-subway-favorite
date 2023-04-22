package nextstep.config;

import lombok.RequiredArgsConstructor;
import nextstep.config.data.UserSession;
import nextstep.exception.InvalidTokenException;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.TokenRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(TokenRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jws = webRequest.getHeader("Authorization");

        if (jwtTokenProvider.validateToken(jws)) {
            throw new InvalidTokenException();
        }
        String principal = jwtTokenProvider.getPrincipal(jws);

        return new UserSession(Long.parseLong(principal));
    }
}
