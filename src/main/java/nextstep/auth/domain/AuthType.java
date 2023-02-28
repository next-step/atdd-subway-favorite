package nextstep.auth.domain;

import nextstep.member.domain.Member;

public interface AuthType {
    boolean match(String header);

    Member findMember(String header);

    void validate(String header);
}
