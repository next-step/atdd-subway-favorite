package nextstep.authentication.domain;

public class LoginMember {

    private String email;
    private Long id;

    public LoginMember(String email, Long id) {
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }
}
