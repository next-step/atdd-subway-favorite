package nextstep.auth.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationToken {

    private String principal;
    private String credentials;

}
