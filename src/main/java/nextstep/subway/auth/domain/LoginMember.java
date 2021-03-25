package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private String password;

    public LoginMember(){

    }

    public LoginMember(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
