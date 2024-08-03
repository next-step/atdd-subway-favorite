package nextstep.subway.auth.application.dto;

public class UserDetailsRequest {
    private String email;
    private String password;

    public UserDetailsRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserDetailsRequest(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
