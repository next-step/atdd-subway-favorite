package nextstep.member.application.dto;

public class ApplicationTokenRequest {
    private String email;
    private String password;

    public ApplicationTokenRequest() {
    }

    public ApplicationTokenRequest(String email, String password) {
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
