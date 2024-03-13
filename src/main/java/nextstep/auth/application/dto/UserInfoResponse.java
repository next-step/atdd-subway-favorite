package nextstep.auth.application.dto;

public class UserInfoResponse {

    private String email;

    public UserInfoResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
