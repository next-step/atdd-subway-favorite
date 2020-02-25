package atdd.member.security;

import atdd.member.dao.MemberDao;
import atdd.member.exception.UserDisabledException;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {


    private final MemberDao memberDao;

    public LoginHandlerMethodArgumentResolver(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String email = (String) webRequest.getAttribute("loginEmail", RequestAttributes.SCOPE_REQUEST);

        return Optional.ofNullable(email)
            .map(memberDao::findByEmail)
            .orElseThrow(UserDisabledException::new);
    }
}
