package nextstep.subway.auth.application;

import nextstep.subway.auth.application.dto.UserDetailsRequest;
import nextstep.subway.member.domain.Member;

public interface UserDetailsService {
    public Member getMember(UserDetailsRequest request);
}
