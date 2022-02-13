package nextstep.subway.unit.auth;

import nextstep.auth.authorization.strategy.SecurityContextHolderStrategy;
import nextstep.auth.context.SecurityContext;

import javax.servlet.http.HttpServletRequest;

import static nextstep.subway.unit.auth.AuthFixture.createAuthentication;

public class FakeSecurityContextHolderStrategy extends SecurityContextHolderStrategy {
    @Override
    public SecurityContext extract(HttpServletRequest request) {
        return new SecurityContext(createAuthentication());
    }
}
