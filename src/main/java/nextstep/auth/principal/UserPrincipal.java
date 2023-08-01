package nextstep.auth.principal;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserPrincipal {

    private String username;

    private String role;

}
