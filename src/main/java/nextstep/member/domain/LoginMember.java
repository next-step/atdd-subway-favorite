package nextstep.member.domain;


import nextstep.auth.authentication.AuthMember;

import java.util.List;

public class LoginMember implements AuthMember {
    private Long id;
    private String email;
    private String password;
    private List<String> authorities;

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword(), member.getRoles());
    }

    public static LoginMember of(String email, List<String> authorities) {
        return new LoginMember(null, email, null, authorities);
    }

    public static LoginMember guest() {
        return new LoginMember();
    }

    public LoginMember() {
    }

    public LoginMember(Long id, String email, String password, List<String> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
