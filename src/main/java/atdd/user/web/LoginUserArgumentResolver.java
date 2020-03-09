package atdd.user.web;

import atdd.user.application.exception.FailedLoginException;
import atdd.user.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    private static final String LOGIN_USER_EMAIL = "loginUserEmail";

    public LoginUserArgumentResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String email = (String) webRequest.getAttribute(LOGIN_USER_EMAIL, SCOPE_REQUEST);
        if (Strings.isBlank(email)) {
            throw new FailedLoginException.InvalidJwtAuthenticationException("Invalid Token");
        }

        return Optional.ofNullable(userRepository.findUserByEmail(email))
                .orElseThrow(() -> new FailedLoginException.InvalidJwtAuthenticationException("Invalid Token"));

    }
}
