package nextstep.auth.resolver;

import nextstep.auth.Interceptor.AuthenticationInterceptor;
import nextstep.auth.annotation.MyInfo;
import nextstep.member.domain.Member;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

public class MyInfoResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(MyInfo.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        Member myInfo = (Member) httpServletRequest.getAttribute(AuthenticationInterceptor.MY_INFO_ATTR_NAME);

        if (myInfo == null) {
            throw new IllegalArgumentException(HttpHeaders.AUTHORIZATION);
        }

        return myInfo;
    }
}
