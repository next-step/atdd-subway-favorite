package nextstep.auth.domain;

import nextstep.member.domain.Member;

public interface AuthService {
    Member findMember(String header);

    void validate(String header);

    String getPrefix();
}
