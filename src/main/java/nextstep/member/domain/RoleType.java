package nextstep.member.domain;

import java.util.List;
import java.util.stream.Collectors;

public enum RoleType {
    ROLE_ADMIN,
    ROLE_MEMBER;

    public static List<RoleType> convert(List<String> roles) {

        return roles.stream()
                .map(it -> valueOf(RoleType.class, it))
                .collect(Collectors.toList());
    }
}
