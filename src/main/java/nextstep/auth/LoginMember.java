package nextstep.auth;

import java.util.List;

public class LoginMember {

    private final Long id;
    private final List<String> roles;

    public LoginMember(Long id, List<String> roles) {
        this.id = id;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public List<String> getRoles() {
        return roles;
    }
}
