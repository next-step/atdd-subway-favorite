package subway.auth.principal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPrincipal {
    private String username;
    private String role;

    public UserPrincipal(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
