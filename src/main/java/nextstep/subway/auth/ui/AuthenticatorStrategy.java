package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.Authentication;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticatorStrategy {

    void saveAuthentication(Authentication authentication, HttpServletResponse response) throws IOException;
}
