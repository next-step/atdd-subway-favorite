package nextstep.member.domain;


import java.util.List;

public class LoginMember {
    private final String email;
    private final String password;
    private final List<String> authorities;

    public static LoginMember of(Member member) {
        return new LoginMember(member.getEmail(), member.getPassword(), member.getRoles());
    }

    public static LoginMember of(String email, List<String> authorities) {
        return new LoginMember(email, null, authorities);
    }

    private LoginMember(String email, String password, List<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String getEmail() {
        return email;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}
