package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    public Optional<LoginMember> getLoginMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() == null) {
            return Optional.empty();
        }
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();

        return Optional.of(loginMember);
    }
}
