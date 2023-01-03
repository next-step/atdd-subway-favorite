package nextstep.utils;

import java.util.Arrays;
import java.util.Objects;

public enum GithubResponses {
    사용자1("1", "access_token_1", "email1@email.com"),
    사용자2("2", "access_token_2", "email2@email.com"),
    사용자3("3", "access_token_3", "email3@email.com"),
    사용자4("4", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    GithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubResponses findByCode(String code) {
        return Arrays.stream(values())
                .filter(it -> Objects.equals(it.code, code))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public static GithubResponses findByToken(String accessToken) {
        return Arrays.stream(values())
                .filter(it -> Objects.equals(it.accessToken, accessToken))
                .findFirst()
                .orElseThrow(RuntimeException::new);
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

