package nextstep.member.domain;

public class LoginMember {
    private final Long id;
    private final String email;

    public LoginMember(final Long id, final String email) {
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
