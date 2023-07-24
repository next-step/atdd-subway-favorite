package subway.auth.token.oauth2;

import subway.member.domain.RoleType;

public interface OAuth2User {
    String getUsername();

    RoleType getRole();
}
