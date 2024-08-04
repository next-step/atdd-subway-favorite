package nextstep.subway.auth.application.dto;

public class MemberTokenRequest {
    private String email;
    private String password;

    public MemberTokenRequest() {
    }

    public MemberTokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
