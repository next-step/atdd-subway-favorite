package subway.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ROLE_MEMBER("ROLE_MEMBER", "사용자"),
    ROLE_ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String name;
}
