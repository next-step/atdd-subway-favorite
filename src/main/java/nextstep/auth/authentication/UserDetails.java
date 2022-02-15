package nextstep.auth.authentication;

import nextstep.member.domain.Member;

import java.util.Objects;

public class UserDetails {
    private Long id;
    private String email;
    private String password;

    public UserDetails(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static UserDetails ofMember(Member member) {
        return new UserDetails(member.getId(), member.getEmail(), member.getPassword());
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDetails that = (UserDetails) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }
}
