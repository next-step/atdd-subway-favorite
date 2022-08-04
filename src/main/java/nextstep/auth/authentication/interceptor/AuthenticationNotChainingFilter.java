package nextstep.auth.authentication.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.user.UserDetails;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthenticationNotChainingFilter implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    AuthenticationToken token = convert(request);
    Authentication authenticate = authenticate(createUserDetails(token), token);
    afterAuthentication(authenticate, response);

    return false;
  }

  public Authentication authenticate(UserDetails userDetails, AuthenticationToken token) {
    if (userDetails == null) {
      throw new AuthenticationException();
    }

    if (!userDetails.isValid(token.getCredentials())) {
      throw new AuthenticationException();
    }

    return new Authentication(userDetails.getPrincipal(), userDetails.getAuthorities());
  }

  public abstract AuthenticationToken convert(HttpServletRequest request) throws Exception;
  public abstract UserDetails createUserDetails(AuthenticationToken token);
  public abstract void afterAuthentication(Authentication authenticate, HttpServletResponse response) throws Exception;
}
