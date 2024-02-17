package nextstep.utils;

import java.util.Arrays;

public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 20),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 30),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 25),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 19),
    잘못된_사용자("wrong_code", "", "", 1);

    private final String code;
    private final String accessToken;
    private final String email;
    private final Integer age;

    GithubResponses(final String code, final String accessToken, final String email, final Integer age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static GithubResponses findByCode(final String code) {
        return Arrays.stream(values())
                .filter(githubResponses -> githubResponses.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않은 code : %s", code)));
    }

    public static GithubResponses findByAccessToken(final String accessToken) {
        return Arrays.stream(values())
                .filter(githubResponses -> githubResponses.accessToken.equals(accessToken))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("유효하지 않은 accessToken : %s", accessToken)));
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
