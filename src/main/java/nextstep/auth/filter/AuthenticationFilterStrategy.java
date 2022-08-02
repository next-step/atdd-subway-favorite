package nextstep.auth.filter;

import nextstep.auth.context.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface AuthenticationFilterStrategy {
    Authentication getAuthentication(HttpServletRequest request);

    void execute(HttpServletResponse response, String email, List<String> authorities) throws IOException;
}
