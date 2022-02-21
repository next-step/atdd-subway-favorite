package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.user.UserDetail;
import nextstep.auth.user.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

  private final UserDetailsService userDetailsService;

  private final AuthenticationConverter authenticationConverter;

  public AuthenticationInterceptor(UserDetailsService userDetailsService, AuthenticationConverter authenticationConverter) {
    this.userDetailsService = userDetailsService;
    this.authenticationConverter = authenticationConverter;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
    AuthenticationToken token = authenticationConverter.convert(request);
    Authentication authentication = authenticate(token);
    afterAuthentication(request, response, authentication);
    return false;
  }

  public Authentication authenticate(AuthenticationToken authenticationToken) {
    UserDetail userDetail = userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());

    validateAuthentication(userDetail, authenticationToken.getCredentials());
    return new Authentication(userDetail);
  }

  public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

  private void validateAuthentication(UserDetail userDetails, String credentials) {

    if (userDetails == null) {
      throw new AuthenticationException();
    }

    if (!userDetails.checkPassword(credentials)) {
      throw new AuthenticationException();
    }
  }

}
