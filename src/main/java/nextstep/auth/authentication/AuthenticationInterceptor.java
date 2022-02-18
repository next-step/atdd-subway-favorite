package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationInterceptor {
	void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
