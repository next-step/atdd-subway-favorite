package nextstep.subway.member.application.dto;

public class EmailPasswordTokenRequest {
    private String email;
    private String password;

    public EmailPasswordTokenRequest() {
    }

    public EmailPasswordTokenRequest(String email, String password) {
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
