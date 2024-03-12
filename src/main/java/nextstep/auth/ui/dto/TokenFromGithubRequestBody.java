package nextstep.auth.ui.dto;

public class TokenFromGithubRequestBody {
    private String code;

    protected TokenFromGithubRequestBody() {
    }

    public TokenFromGithubRequestBody(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
