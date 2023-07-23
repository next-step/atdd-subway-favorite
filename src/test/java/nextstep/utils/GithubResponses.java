package nextstep.utils;

import java.util.Arrays;
import java.util.Objects;

public enum GithubResponses {

    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    GithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubResponses matchCode(String code) {
        return Arrays.stream(values())
                .filter(githubResponses -> Objects.equals(code, githubResponses.code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 code 입니다."));
    }

    public static GithubResponses matchAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(githubResponses -> Objects.equals(accessToken, githubResponses.accessToken))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 AccessToken 입니다."));
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }
}
