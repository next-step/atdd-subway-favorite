package nextstep.member;

import java.util.List;

public class LoginMember {

    private final String email;
    private final List<String> roles;

    public LoginMember(String email, List<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }
    
}
