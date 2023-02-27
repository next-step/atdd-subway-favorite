package nextstep.subway.acceptance.controller;

import java.util.Arrays;

public enum GithubResponses {
    회원가입_한_사용자1("832ovnq039hfjn", "access_token_1", "member@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "admin@email.com"),
    회원가입_하지_않은_사용자("kaoq2gmq3t2", "access_token_3", "bactoria@email.com");

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

    public String getCode() {
        return code;
    }
}
