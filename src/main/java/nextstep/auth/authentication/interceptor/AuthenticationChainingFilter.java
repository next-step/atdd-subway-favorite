package nextstep.auth.authentication.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.user.UserDetails;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthenticationChainingFilter implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      return true;
    }

    try {
      AuthenticationToken token = convert(request);
      UserDetails userDetails = createUserDetails(token);

      Authentication authentication = new Authentication(userDetails.getPrincipal(), userDetails.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);

      return true;
    } catch (AuthenticationException e) {
      return true;
    }
  }

  abstract AuthenticationToken convert(HttpServletRequest request);
  abstract UserDetails createUserDetails(AuthenticationToken token);
}
