package nextstep.auth.domain;

import java.util.Objects;

public class LoginMember implements UserDetails {
    private Long id;
    private String email;

    public LoginMember(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginMember that = (LoginMember) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
