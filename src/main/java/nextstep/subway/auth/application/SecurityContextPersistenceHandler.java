package nextstep.subway.auth.application;

import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextPersistenceHandler {
    public void persist(SecurityContext securityContext) {
        SecurityContextHolder.setContext(securityContext);
    }
}
