package nextstep.auth.application.dto;

public class UserInfoRequest {

    private String accessToken;

    public UserInfoRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
