package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationConverter {

    AuthenticationToken convert(HttpServletRequest request) throws IOException;

    void afterAuthentication(HttpServletRequest request,
                             HttpServletResponse response,
                             Authentication authentication) throws IOException;
}
