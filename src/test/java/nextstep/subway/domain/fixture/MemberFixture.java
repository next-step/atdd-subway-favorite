package nextstep.subway.domain.fixture;

import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;

import java.util.List;

public class MemberFixture {

    public static final Member MEMBER_A = new Member(1L, "member_a@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name()));
    public static final Member MEMBER_B = new Member(2L, "member_b@email.com", "password", 25, List.of(RoleType.ROLE_MEMBER.name()));

}
