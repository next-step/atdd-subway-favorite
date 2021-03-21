package nextstep.subway.auth.ui;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.subway.auth.application.UserDetailService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.domain.UserDetail;
import nextstep.subway.auth.exception.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {

  private final UserDetailService userDetailsService;
  private final AuthenticationConverter converter;

  public AbstractAuthenticationInterceptor(UserDetailService userDetailsService,
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
    UserDetail userDetail = userDetailsService.loadUserByUsername(principal);
    checkAuthentication(userDetail, authenticationToken);
    return new Authentication(userDetail);
  }

  private void checkAuthentication(UserDetail userDetail, AuthenticationToken token) {
    if (userDetail == null) {
      throw new AuthenticationException("등록되지 않은 사용자 입니다.");
    }

    if (!userDetail.checkPassword(token.getCredentials())) {
      throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
    }
  }
}
