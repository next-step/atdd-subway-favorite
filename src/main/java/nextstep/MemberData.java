package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;

import java.util.List;

public class MemberData {
    private static final String ADMIN_EMAIL = "admin@email.com";
    private static final String ADMIN_PASSWORD = "password";
    private static final int ADMIN_AGE = 20;

    private static final String MEMBER_EMAIL = "email@email.com";
    private static final String MEMBER_PASSWORD = "password";
    private static final int MEMBER_AGE = 20;

    public static Member admin = new Member(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE, List.of(RoleType.ROLE_ADMIN.name()));
    public static Member member = new Member(MEMBER_EMAIL, MEMBER_PASSWORD, MEMBER_AGE, List.of(RoleType.ROLE_MEMBER.name()));
}
