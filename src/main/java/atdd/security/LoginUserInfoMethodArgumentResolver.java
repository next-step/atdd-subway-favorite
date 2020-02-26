package atdd.security;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserInfoMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final LoginUserRegistry loginUserRegistry;

    public LoginUserInfoMethodArgumentResolver(LoginUserRegistry loginUserRegistry) {
        this.loginUserRegistry = loginUserRegistry;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return LoginUserInfo.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        return loginUserRegistry.getCurrentLoginUser();
    }

}
