package nextstep.github;

import java.util.Arrays;

public enum FakeGithubResponse {

    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com"),
    EMPTY("EMPTY", "EMPTY", "EMPTY");

    private String code;
    private String accessToken;
    private String email;

    FakeGithubResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public String getCode() { return code; }
    public String getAccessToken() { return accessToken; }
    public String getEmail() { return email; }

    public static FakeGithubResponse findByCode(String code) {
        return Arrays.asList(FakeGithubResponse.values()).stream()
                .filter(response -> response.equalsByCode(code))
                .findFirst()
                .orElse(EMPTY);
    }

    public static FakeGithubResponse findByAccessToken(String accessToken) {
        return Arrays.asList(FakeGithubResponse.values()).stream()
                .filter(response -> response.equalsByAccessToken(accessToken))
                .findFirst()
                .orElse(EMPTY);
    }

    private boolean equalsByCode(String code) {
        if (this.code.equals(code)) { return true; }
        return false;
    }

    private boolean equalsByAccessToken(String accessToken) {
        if (this.accessToken.equals(accessToken)) { return true; }
        return false;
    }
}