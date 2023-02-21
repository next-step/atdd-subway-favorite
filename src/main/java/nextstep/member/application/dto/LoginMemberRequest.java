package nextstep.member.application.dto;

import java.util.List;

public class LoginMemberRequest {
    private final String email;
    private final List<String> roles;

    public LoginMemberRequest(String email, List<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }
}
