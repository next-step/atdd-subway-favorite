package nextstep.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import nextstep.subway.auth.domain.AuthenticationToken;

public interface AuthenticationConverter {
  AuthenticationToken convert(HttpServletRequest request);
}