package nextstep.member.domain.auth;

import nextstep.member.domain.RoleType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LoginMember {
    private String email;
    private Set<RoleType> roles;

    public LoginMember(String email, List<RoleType> roles) {
        this.email = email;
        this.roles = roles.stream().collect(Collectors.toSet());
    }

    public String getEmail() {
        return email;
    }
}
