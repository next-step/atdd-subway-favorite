package nextstep.auth.domain;

public class ProfileResponse {

    private final String email;

    public ProfileResponse(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
