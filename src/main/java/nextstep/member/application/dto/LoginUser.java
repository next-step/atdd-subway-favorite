package nextstep.member.application.dto;

import java.util.List;

public class LoginUser {
    private Long id;
    private String email;
    private List<String> roles;

    public LoginUser(String id, String email, List<String> roles) {
        this.id = Long.valueOf(id);
        this.email = email;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
