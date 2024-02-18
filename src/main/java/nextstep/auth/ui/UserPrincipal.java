package nextstep.auth.ui;

public class UserPrincipal {
    private final Long id;
    private final String email;

    public UserPrincipal(final Long id, final String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
