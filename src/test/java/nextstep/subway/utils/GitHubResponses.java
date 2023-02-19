package nextstep.subway.utils;

import java.util.Arrays;

public enum GitHubResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com"),
    사용자5_ACCESS_TOKEN_없음("fkqjqlwei120fd", null, "email5@email.com")
    ;

    private final String code;
    private final String accessToken;
    private final String email;

    GitHubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GitHubResponses fromCode(String code) {
        return Arrays.stream(values())
            .filter(it -> it.code.equals(code))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static GitHubResponses fromAccessToken(String accessToken) {
        return Arrays.stream(values())
            .filter(it -> it.accessToken != null && it.accessToken.equals(accessToken))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
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
