package nextstep.member.ui;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.LoginUser;
import nextstep.member.exception.AuthorizationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final String BEARER_TOKEN = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public UserArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authToken = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        String token = validateAuth(authToken);

        String id = jwtTokenProvider.getPrincipal(token);
        String email = jwtTokenProvider.getEmail(token);
        List<String> roles = jwtTokenProvider.getRoles(token);


        return new LoginUser(id, email, roles);
    }

    private String validateAuth(String auth) {
        String scim = auth.substring(0, BEARER_TOKEN.length());
        if (!scim.equals(BEARER_TOKEN)) {
            throw new AuthorizationException("인증 SCIM이 유효하지 않습니다.");
        }

        String token = auth.replace(BEARER_TOKEN, "");
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException("인증 토큰이 유효하지 않습니다.");
        }

        return token;
    }
}
