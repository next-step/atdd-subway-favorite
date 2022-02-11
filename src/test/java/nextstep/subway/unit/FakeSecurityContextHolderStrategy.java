package nextstep.subway.unit;

import nextstep.auth.authorization.SecurityContextHolderStrategy;
import nextstep.auth.context.SecurityContext;

import javax.servlet.http.HttpServletRequest;

import static nextstep.subway.unit.AuthFixture.createAuthentication;

public class FakeSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
    @Override
    public SecurityContext extract(HttpServletRequest request) {
        return new SecurityContext(createAuthentication());
    }
}
