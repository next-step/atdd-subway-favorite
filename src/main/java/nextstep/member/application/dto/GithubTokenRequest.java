package nextstep.member.application.dto;

public class GithubTokenRequest {
    private final String code;

    public GithubTokenRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
