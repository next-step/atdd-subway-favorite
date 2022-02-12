package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@FunctionalInterface
public interface AuthenticationConverter {
    AuthenticationToken convert(HttpServletRequest request) throws IOException;
}
