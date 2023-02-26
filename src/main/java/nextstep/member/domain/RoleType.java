package nextstep.member.domain;

import java.util.List;

public enum RoleType {
    ROLE_ADMIN,
    ROLE_MEMBER;

    public static List<String> getAdminRole(){
        return List.of(ROLE_MEMBER.name(), ROLE_ADMIN.name());
    }

    public static List<String> getMemberRole(){
        return List.of(ROLE_MEMBER.name());
    }
}
