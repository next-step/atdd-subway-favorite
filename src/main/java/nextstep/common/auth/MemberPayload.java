package nextstep.common.auth;

import lombok.Getter;
import nextstep.member.domain.RoleType;

import java.util.List;
import java.util.Objects;

@Getter
public class MemberPayload {

    private final String email;
    private final List<String> roles;

    public MemberPayload(final String email, final List<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public boolean isAdmin() {
        return roles.stream()
                .anyMatch(role -> role.equals(RoleType.ROLE_ADMIN.name()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberPayload that = (MemberPayload) o;
        return Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getRoles(), that.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getRoles());
    }
}
