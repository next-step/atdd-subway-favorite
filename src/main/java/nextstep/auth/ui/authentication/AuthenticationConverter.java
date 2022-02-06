package nextstep.auth.ui.authentication;

import nextstep.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AuthenticationConverter {
    AuthenticationToken convert(HttpServletRequest request) throws IOException;
}
