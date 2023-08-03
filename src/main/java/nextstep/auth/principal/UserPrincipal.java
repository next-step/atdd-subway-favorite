package nextstep.auth.principal;

import lombok.Builder;
import lombok.Getter;

@Builder
public class UserPrincipal {

    private String username;

    @Getter
    private String role;

    public String getEmail() {
        return username;
    }

}
