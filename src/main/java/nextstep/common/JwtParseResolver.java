package nextstep.common;

import nextstep.common.exception.ErrorResponse;
import nextstep.common.exception.TokenException;
import nextstep.member.application.TokenService;
import nextstep.member.domain.Member;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class JwtParseResolver implements HandlerMethodArgumentResolver {

    private final TokenService tokenService;

    public JwtParseResolver(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        final boolean isTokenType = parameter.getParameterAnnotation(LoginMember.class) != null;
        final boolean isLong = Long.class.equals(parameter.getParameterType());
        return isTokenType && isLong;
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory)
            throws Exception {
        final String jwtToken = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken == null) {
            throw new TokenException(ErrorResponse.INVALID_TOKEN_VALUE);
        }
        return tokenService.getMember(jwtToken).getId();
    }
}
