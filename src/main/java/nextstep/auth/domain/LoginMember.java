package nextstep.auth.domain;

public class LoginMember {
    private Long id;
    private String email;

    public LoginMember() {
    }

    public LoginMember(Long id, String email) {
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
