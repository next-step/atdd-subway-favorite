package atdd.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import atdd.auth.application.AuthConstants;
import atdd.auth.application.AuthService;
import atdd.auth.application.dto.AuthInfoView;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
  private AuthService authService;

  @Autowired
  public AuthorizationInterceptor(AuthService authService) {
    this.authService = authService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    String email = authService.authUser(new AuthInfoView(authToken));
    request.setAttribute(AuthConstants.UserEmailAttribute, email);
    return super.preHandle(request, response, handler);
  }
}
