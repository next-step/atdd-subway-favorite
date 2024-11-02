package nextstep.member.application.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GithubTokenRequest {

    private String code;

    public GithubTokenRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
