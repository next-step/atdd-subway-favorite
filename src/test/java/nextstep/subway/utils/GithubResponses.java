package nextstep.subway.utils;

import java.util.Arrays;

public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    GithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubResponses 코드로_사용자_조회(String code) {
        return Arrays.stream(values())
                .filter(response -> response.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    public static GithubResponses 토큰으로_사용자_조회(String token) {
        return Arrays.stream(values())
                .filter(response -> response.getAccessToken().equals(token))
                .findFirst()
                .orElse(null);
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
