package nextstep.subway.auth.ui.authentication;

import nextstep.subway.auth.domain.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionAuthenticationInterceptor extends AuthenticationInterceptor{
    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

    }
}
