package nextstep.member.domain;

import nextstep.auth.user.User;

import java.util.List;

public class LoginMember {


    private String email;

    private String password;

    private List<String> authorities;

    public LoginMember(String email, String password, List<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static LoginMember of(String email, String password, List<String> authorities) {
        return new LoginMember(email, password, authorities);
    }

    public User toUser() {
        return new User(email, password, authorities);
    }


}
