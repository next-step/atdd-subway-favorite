package nextstep.member.ui;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.TokenInfo;
import nextstep.member.domain.LoginMember;
import nextstep.member.exception.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        final String token = extractTokenFrom(webRequest);
        final TokenInfo tokenInfo = jwtTokenProvider.getPrincipal(token);
        return new LoginMember(tokenInfo.getId(), tokenInfo.getEmail());
    }

    private String extractTokenFrom(final NativeWebRequest webRequest) {
        final String authorization = webRequest.getHeader("Authorization");
        if (!StringUtils.hasLength(authorization)) {
            throw new AuthenticationException();
        }

        final String[] splitAuth = authorization.split(" ");
        if (splitAuth.length != 2) {
            throw new AuthenticationException();
        }

        if (!"bearer".equalsIgnoreCase(splitAuth[0])) {
            throw new AuthenticationException();
        }
        final String token = splitAuth[1];

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }
        return token;
    }
}
