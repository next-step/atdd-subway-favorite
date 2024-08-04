package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.MemberDetails;

public interface MemberDetailsService {
    MemberDetails findByEmailOrCreateMember(String email);
}
