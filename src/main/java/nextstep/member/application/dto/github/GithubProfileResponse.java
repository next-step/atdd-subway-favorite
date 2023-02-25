package nextstep.member.application;

public class GithubProfileResponse {
    private final String code;
    private final String clientId;
    private final String clientSecret;

    public GithubProfileResponse(String code, String clientId, String clientSecret) {
        this.code = code;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
