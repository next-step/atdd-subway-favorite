package atdd.auth.interceptor;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import org.springframework.beans.factory.annotation.Autowired;

import atdd.auth.LoginUser;
import atdd.auth.application.AuthConstants;
import atdd.user.application.UserService;

@Component
public class AuthorizationArgumentResolver implements HandlerMethodArgumentResolver{
  private UserService userService;
  
  @Autowired
  public AuthorizationArgumentResolver(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    String email = (String) webRequest.getAttribute(AuthConstants.UserEmailAttribute, SCOPE_REQUEST);

    return userService.retrieveUserByEmail(email);
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(LoginUser.class);
  }

}
