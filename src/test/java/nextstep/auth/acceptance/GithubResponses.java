package nextstep.auth.acceptance;

import java.util.Arrays;

public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 14),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 21),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 28),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 35),
    사용자_인증X("user", "", "", 99);

    private String code;
    private String accessToken;
    private String email;
    private int age;

    GithubResponses(String code, String accessToken, String email, Integer age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
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

    public static GithubResponses findByCode(final String code) {
        return Arrays.stream(values())
                .filter(githubResponses -> githubResponses.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 코드입니다. %s", code)));
    }

    public static GithubResponses findByAccessToken(final String accessToken) {
        return Arrays.stream(values())
                .filter(githubResponses -> githubResponses.accessToken.equals(accessToken))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("유효하지 않은 토큰입니다. %s", accessToken)));
    }
}