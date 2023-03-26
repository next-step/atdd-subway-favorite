package nextstep.member.domain;

import java.util.List;

public enum RoleType {
    ROLE_ADMIN,
    ROLE_MEMBER;

    public static List<String> memberUser() {
        return List.of(ROLE_MEMBER.name());
    }
}
