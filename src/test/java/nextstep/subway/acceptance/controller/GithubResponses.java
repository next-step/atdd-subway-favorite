package nextstep.subway.acceptance.controller;

import java.util.Arrays;

public enum GithubResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "member@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "admin@email.com");

    private String code;
    private String accessToken;
    private String email;

    GithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubResponses fromCode(String code) {
        return Arrays.stream(values())
                .filter(it -> it.code.equals(code))
                .findFirst()
                .orElse(null);
    }

    public static GithubResponses fromAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(it -> it.accessToken.equals(accessToken))
                .findFirst()
                .orElse(null);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return email;
    }
}
