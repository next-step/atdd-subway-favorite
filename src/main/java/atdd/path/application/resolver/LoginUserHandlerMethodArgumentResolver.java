package atdd.path.application.resolver;

import atdd.path.application.exception.InvalidJwtAuthenticationException;
import atdd.path.dao.MemberDao;
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

    private final MemberDao memberDao;

    public LoginUserHandlerMethodArgumentResolver(MemberDao memberDao) {
        this.memberDao = memberDao;
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

        return memberDao.findByEmail(email).orElseThrow(InvalidJwtAuthenticationException::new);
    }

}
