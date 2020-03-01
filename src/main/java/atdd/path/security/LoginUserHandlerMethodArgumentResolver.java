package atdd.path.security;

import atdd.path.application.JwtTokenProvider;
import atdd.path.dao.UserDao;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserDao userDao;
    private JwtTokenProvider jwtTokenProvider;

    public LoginUserHandlerMethodArgumentResolver(UserDao userDao, JwtTokenProvider jwtTokenProvider) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUserEmail(accessToken);
        return userDao.findByEmail(email);
    }
}
