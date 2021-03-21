package nextstep.subway.auth.ui;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {

  private final CustomUserDetailsService userDetailsService;
  private final AuthenticationConverter converter;

  public AbstractAuthenticationInterceptor(CustomUserDetailsService userDetailsService,
      AuthenticationConverter authenticationConverter) {
    this.userDetailsService = userDetailsService;
    this.converter = authenticationConverter;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) throws IOException {
    AuthenticationToken token = converter.convert(request);
    Authentication authentication = authenticate(token);
    afterAuthentication(request, response, authentication);
    return false;
  }

  protected abstract void afterAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException;

  private Authentication authenticate(AuthenticationToken authenticationToken) {
    String principal = authenticationToken.getPrincipal();
    LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
    checkAuthentication(userDetails, authenticationToken);
    return new Authentication(userDetails);
  }

  private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
    if (userDetails == null) {
      throw new RuntimeException();
    }

    if (!userDetails.checkPassword(token.getCredentials())) {
      throw new RuntimeException();
    }
  }
}
