package nextstep.test;

import java.util.Arrays;

public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com"),
    이미_가입된_사용자("code", "access_token5", "admin@email.com");

    private String code;
    private String accessToken;
    private String email;

    GithubResponses(String code, String accessToken, String email) {
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

    public static GithubResponses from(String code) {
        return Arrays.stream(GithubResponses.values())
            .filter(githubResponses -> githubResponses.code.equals(code))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코드입니다."));
    }

    public static GithubResponses fromAccessToken(String accessToken) {
        return Arrays.stream(GithubResponses.values())
            .filter(githubResponses -> githubResponses.accessToken.equals(accessToken))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 액세스 토큰입니다."));
    }
}
