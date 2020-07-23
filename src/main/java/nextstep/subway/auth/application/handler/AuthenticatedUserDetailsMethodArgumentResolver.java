package nextstep.subway.auth.application.handler;

import nextstep.subway.auth.application.UserDetail;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticatedUserDetailsMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class) &&
                parameter.getParameterType().equals(UserDetail.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        return authentication.getPrincipal();
    }
}
