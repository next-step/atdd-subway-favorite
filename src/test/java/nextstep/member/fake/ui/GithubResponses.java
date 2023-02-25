package nextstep.member.fake.ui;

import java.util.Arrays;
import java.util.Objects;

public enum GithubResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

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

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
