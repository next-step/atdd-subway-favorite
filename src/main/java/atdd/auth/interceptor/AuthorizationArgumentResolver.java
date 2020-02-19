package atdd.auth.interceptor;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthorizationArgumentResolver implements HandlerMethodArgumentResolver{

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    // TODO Auto-generated method stub
    return false;
  }

}
