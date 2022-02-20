package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationConverter {
	AuthenticationToken convert(HttpServletRequest request);
}
