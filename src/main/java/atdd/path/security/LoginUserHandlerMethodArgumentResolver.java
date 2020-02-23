package atdd.path.security;

import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserDao userDao;

    public LoginUserHandlerMethodArgumentResolver(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String email = (String) webRequest.getAttribute("loginUserEmail", SCOPE_REQUEST);
        if (Strings.isBlank(email)) {
            return null;
        }
        User user = userDao.findByEmail(email);
        return userDao.findByEmail(email);
    }
}
