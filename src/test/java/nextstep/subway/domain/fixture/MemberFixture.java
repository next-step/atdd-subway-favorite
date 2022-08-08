package nextstep.subway.domain.fixture;

import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;

import java.util.List;

public class MemberFixture {

    public static final Member MEMBER = new Member(1L, "member@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name()));

}
