package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface AuthenticationConverter {
  AuthenticationToken convert(HttpServletRequest request);
}