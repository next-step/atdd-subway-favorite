package nextstep.config;

import lombok.RequiredArgsConstructor;
import nextstep.config.data.UserSession;
import nextstep.exception.Unauthorized;
import nextstep.member.application.JwtTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jws = webRequest.getHeader(AUTHORIZATION);

        if (!jwtTokenProvider.validateToken(jws)) {
            throw new Unauthorized();
        }
        String principal = jwtTokenProvider.getPrincipal(jws);

        return new UserSession(Long.parseLong(principal));
    }
}
