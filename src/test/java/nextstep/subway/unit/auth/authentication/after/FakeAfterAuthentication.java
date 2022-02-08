package nextstep.subway.unit.auth.authentication.after;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nextstep.auth.authentication.after.AfterAuthentication;
import nextstep.auth.context.Authentication;

public class FakeAfterAuthentication implements AfterAuthentication {
    private Authentication authentication;

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException {
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}
