package atdd.favorite.application.dto;

import atdd.user.application.UserService;
import atdd.user.domain.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class LoginUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private UserService userService;

    public LoginUserMethodArgumentResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public User resolveArgument(MethodParameter parameter,
                                ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest,
                                WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest=(HttpServletRequest)webRequest.getNativeRequest();
        String email = (String)httpServletRequest.getAttribute("email");
        if (Strings.isBlank(email)) {
            return null;
        }
        User user = userService.findByEmail(email);
        return user;
    }
}
