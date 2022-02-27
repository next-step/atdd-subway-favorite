package nextstep.auth.authentication.converter;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.authentication.AuthenticationToken;

public interface AuthenticationConverter {
    AuthenticationToken convert(HttpServletRequest request) throws IOException;
}
