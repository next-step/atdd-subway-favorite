package subway.auth.userdetails;

import subway.member.domain.RoleType;

public interface UserDetails {
    String getUsername();

    String getPassword();

    RoleType getRole();
}
