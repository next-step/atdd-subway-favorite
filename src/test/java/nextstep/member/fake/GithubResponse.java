package nextstep.member.fake;

import nextstep.common.exception.ErrorResponse;
import nextstep.common.exception.LoginException;

import java.util.Arrays;

public enum GithubResponse {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");;

    private final String code;
    private final String accessToken;
    private final String email;

    GithubResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public static GithubResponse getByCode(String code) {
        return Arrays.stream(values())
                .filter(githubResponse -> githubResponse.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new LoginException(ErrorResponse.INVALIDATION_LOGIN_INFORMATION));
    }

    public static GithubResponse getByAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(githubResponse -> githubResponse.getAccessToken().equals(accessToken))
                .findFirst()
                .orElseThrow(() -> new LoginException(ErrorResponse.INVALIDATION_LOGIN_INFORMATION));
    }
}
