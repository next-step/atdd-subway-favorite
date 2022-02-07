package nextstep.subway.auth.ui.authentication;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AuthenticationConverter {
    AuthenticationToken convert(HttpServletRequest request) throws IOException;
}
