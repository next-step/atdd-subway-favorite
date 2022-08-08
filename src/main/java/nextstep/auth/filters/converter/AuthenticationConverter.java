package nextstep.auth.filters.converter;

import nextstep.auth.authentication.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@FunctionalInterface
public interface AuthenticationConverter {
    AuthenticationToken convert(HttpServletRequest request) throws IOException;
}
