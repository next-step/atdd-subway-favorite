package nextstep.core.member.application.dto;

public class GithubProfileRequest {
    String code;

    public GithubProfileRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
