package nextstep.auth.infra;

import java.util.Arrays;

public enum GithubResponses {
    USER1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    USER2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    USER3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    USER4("nvci383mciq0oq", "access_token_4", "email4@email.com"),
    ;

    private String code;
    private String accessToken;
    private String email;

    GithubResponses(final String code, final String accessToken, final String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubResponses find(final String code) {
        return Arrays.stream(values())
                .filter(responses -> responses.code.equals(code))
                .findAny()
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
