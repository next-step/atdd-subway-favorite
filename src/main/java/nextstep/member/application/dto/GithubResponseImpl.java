package nextstep.member.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubResponseImpl implements GithubResponse {

    private String code;

    private String accessToken;

    private String email;

    @Builder
    private GithubResponseImpl(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public String getEmail() {
        return this.email;
    }
}
