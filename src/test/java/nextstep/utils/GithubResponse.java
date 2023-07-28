package nextstep.utils;

import java.util.Arrays;
import nextstep.auth.AuthenticationException;

public enum GithubResponse {

    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    public String getCode() {
        return code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    GithubResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubResponse findByCode(String code) {

        return Arrays.stream(GithubResponse.values())
            .filter(githubResponse -> githubResponse.getCode().equals(code))
            .findFirst()
            .orElseThrow(AuthenticationException::new);
    }

    public static GithubResponse findByToken(String token) {
        return Arrays.stream(GithubResponse.values())
            .filter(githubResponse -> githubResponse.getAccessToken().equals(token))
            .findFirst()
            .orElseThrow(AuthenticationException::new);
    }
}
