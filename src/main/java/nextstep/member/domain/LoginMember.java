package nextstep.member.domain;

import java.util.List;

public class LoginMember {
    private Long id;
    private List<String> roles;

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
