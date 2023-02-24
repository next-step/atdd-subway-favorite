package nextstep.member.application.dto;

public class GithubLoginRequest {

    private final String code;

    public GithubLoginRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
