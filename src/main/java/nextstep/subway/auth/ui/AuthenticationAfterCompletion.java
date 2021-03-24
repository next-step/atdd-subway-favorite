package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract  class AuthenticationAfterCompletion {
    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
