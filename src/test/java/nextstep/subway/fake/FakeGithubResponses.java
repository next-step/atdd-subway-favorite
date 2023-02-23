package nextstep.subway.fake;

import java.util.Arrays;

public enum FakeGithubResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    FakeGithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static String findAccessToken(String code) {
        return Arrays.stream(values())
            .filter(response -> response.code.equals(code))
            .map(response -> response.accessToken)
            .findFirst()
            .orElseThrow();
    }

    public static String findEmail(String accessToken) {
        return Arrays.stream(values())
            .filter(response -> response.accessToken.equals(accessToken))
            .map(response -> response.email)
            .findFirst()
            .orElseThrow();
    }

    public String getCode() {
        return code;
    }
}
