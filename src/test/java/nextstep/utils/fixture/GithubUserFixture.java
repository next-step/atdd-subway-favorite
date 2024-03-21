package nextstep.utils.fixture;

import java.util.Arrays;

public enum GithubUserFixture {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    GithubUserFixture(String code, String accessToken, String email) {
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

    public static GithubUserFixture findByAccessToken(String accessToken) {
        return Arrays.stream(GithubUserFixture.values())
                .filter(githubUserFixture -> githubUserFixture.getAccessToken().equals(accessToken))
                .findFirst().orElseThrow();
    }

    public static GithubUserFixture findByCode(String code) {
        return Arrays.stream(GithubUserFixture.values())
                .filter(githubUserFixture -> githubUserFixture.getCode().equals(code))
                .findFirst().orElseThrow();
    }
}
