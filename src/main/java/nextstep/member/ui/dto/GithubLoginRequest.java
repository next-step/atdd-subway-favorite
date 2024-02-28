package nextstep.member.ui.dto;

public class GithubLoginRequest {
    private String code;

    public GithubLoginRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
