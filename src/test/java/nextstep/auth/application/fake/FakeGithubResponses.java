package nextstep.auth.application.fake;

import java.util.Arrays;

public enum FakeGithubResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    FakeGithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static FakeGithubResponses getGithubResponseByCode(String code) {
        return Arrays.stream(FakeGithubResponses.values())
                .filter(a -> code.equals(a.code))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public static FakeGithubResponses getGithubResponseByAccessToken(String accessToken) {
        return Arrays.stream(FakeGithubResponses.values())
                .filter(a -> accessToken.equals(a.accessToken))
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
