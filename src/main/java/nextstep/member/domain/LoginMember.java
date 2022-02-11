package nextstep.member.domain;

import nextstep.auth.manager.UserMember;

public class LoginMember implements UserMember {
    private Long id;
    private String email;
    private String password;

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword());
    }

    public LoginMember(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
