package nextstep.auth.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationConverter {
	AuthenticationToken convert(HttpServletRequest request) throws IOException;
}
