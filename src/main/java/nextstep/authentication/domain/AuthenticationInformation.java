package nextstep.authentication.domain;

import nextstep.authentication.application.exception.AuthenticationException;

public class AuthenticationInformation {

    private final String email;
    private final Long id;
    private final String password;

    public AuthenticationInformation(String email, Long id, String password) {
        this.email = email;
        this.id = id;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void verification(String password) {
        if (!this.password.equals(password)) {
            throw new AuthenticationException();
        }
    }
}
