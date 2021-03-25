package nextstep.subway.auth.ui.common;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@FunctionalInterface
public interface AuthenticationConverter {
    AuthenticationToken convert(HttpServletRequest request) throws IOException;
}
