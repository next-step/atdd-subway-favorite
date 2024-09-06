package nextstep.authentication.application.dto;

public class GithubCodeRequest {
    private String code;

    public GithubCodeRequest() {
    }

    public GithubCodeRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
