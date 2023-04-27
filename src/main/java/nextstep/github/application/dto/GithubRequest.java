package nextstep.github.application.dto;


public class GithubRequest {
    private String code;

    public GithubRequest() {
    }

    public GithubRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
