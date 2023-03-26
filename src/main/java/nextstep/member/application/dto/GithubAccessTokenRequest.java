package nextstep.member.application.dto;

public class GithubAccessTokenRequest {

    String code;
    String clientId;
    String clientSecret;

    public GithubAccessTokenRequest(String code, String clientId, String clientSecret) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
