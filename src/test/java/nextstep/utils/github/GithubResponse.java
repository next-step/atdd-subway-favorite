package nextstep.utils.github;

import java.util.Arrays;

public enum GithubResponse {
    사용자1("s1emn12lkm", "access_token_1", "email1@email.com", 10),
    사용자2("asdfavvw3122", "access_token_2", "email2@email.com", 12),
    사용자3("fefef2121212", "access_token_3", "email3@email.com", 13),
    사용자4("dfdhhtgrwcxz", "access_token_4", "email4@email.com", 15),
    ;
    private String code;
    private String accessToken;
    private String email;
    private Integer age;

    GithubResponse(String code, String accessToken, String email, Integer age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static GithubResponse valueOfCode(String code) {
        return Arrays.stream(values())
                .filter(value -> value.code.equals(code))
                .findAny()
                .orElse(null);
    }

    public static GithubResponse valueOfAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(value -> value.accessToken.equals(accessToken))
                .findAny()
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

    public Integer getAge() {
        return age;
    }
}