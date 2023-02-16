package nextstep.member;

import java.util.List;

public class LoginMember {

    private final String principal;
    private final List<String> roles;

    public LoginMember(String principal, List<String> roles) {
        this.principal = principal;
        this.roles = roles;
    }
}
