package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@FunctionalInterface
public interface AuthenticationConverter {
    AuthenticationToken convert(HttpServletRequest request) throws IOException;
}
