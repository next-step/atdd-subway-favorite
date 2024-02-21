package nextstep.auth.principal;

import lombok.extern.slf4j.Slf4j;
import nextstep.auth.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import org.jgrapht.alg.util.Pair;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");
        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0]) || authorization.split(" ").length <= 1) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];

        Pair<Long, String> tokenBody = jwtTokenProvider.getPrincipal(token);
        Long id = tokenBody.getFirst();
        String email = tokenBody.getSecond();

        return new LoginMember(id, email);
    }
}
