package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AuthenticationTokenConverter {

    AuthenticationToken convert(HttpServletRequest request) throws IOException;
}
