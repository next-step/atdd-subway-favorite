package nextstep.utils.github;

import java.util.Arrays;

public enum GithubResponse {
    사용자1("s1emn12lkm", "access_token_1", "email1@email.com"),
    사용자2("asdfavvw3122", "access_token_2", "email2@email.com"),
    사용자3("fefef2121212", "access_token_3", "email3@email.com"),
    사용자4("dfdhhtgrwcxz", "access_token_4", "email4@email.com"),
    ;
    private String code;
    private String accessToken;
    private String email;

    GithubResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }
    public static GithubResponse valueOfCode(String code) {
        return Arrays.stream(values())
                .filter(value -> value.code.equals(code))
                .findAny()
                .orElse(null);
    }

    public String getCode() {
        return code;
    }
    public String getAccessToken() {
        return accessToken;
    }
}