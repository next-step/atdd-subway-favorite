package nextstep.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ROLE_ADMIN("admin@nextstep.com", "password", 25),
    ROLE_MEMBER("member@nextstep.com", "password", 23);

    private final String email;
    private final String password;
    private final int age;

}
