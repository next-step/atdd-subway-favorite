package nextstep.auth.application;

import java.util.Arrays;

public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com"),
    BLANK("", "", "");

    private String code;
    private String accessToken;
    private String email;

    public static GithubResponses findByCode(String code) {
        return Arrays.stream(values())
                .filter(githubResponses -> githubResponses.code.equals(code))
                .findFirst()
                .orElse(BLANK);
    }

    public static GithubResponses findByAccessToken(String accesstoken) {
        return Arrays.stream(values())
                .filter(githubResponses -> githubResponses.accessToken.equals(accesstoken))
                .findFirst()
                .orElse(BLANK);
    }

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
}
