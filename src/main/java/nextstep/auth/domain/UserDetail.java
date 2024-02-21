package nextstep.auth.domain;

import nextstep.member.domain.Member;

public class UserDetail {
    private String email;
    private String password;

    public UserDetail(Member member) {
        email = member.getEmail();
        password = member.getPassword();
    }

    public UserDetail(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
