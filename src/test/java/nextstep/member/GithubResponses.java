package nextstep.member;

import java.util.Arrays;

public enum GithubResponses {

    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 19),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 20),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 21),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 22);

    private String code;
    private String accessToken;
    private String email;
    private int age;

    GithubResponses(String code, String accessToken, String email, int age) {
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

    public int getAge() {
        return age;
    }

    public static GithubResponses findByCode(String code) {
        return Arrays.stream(values())
            .filter(e -> code.equals(e.code))
            .findAny()
            .orElseThrow(AuthenticationException::new);
    }

    public static GithubResponses findByAccessToken(String accessToken) {
        return Arrays.stream(values())
            .filter(e -> accessToken.equals(e.accessToken))
            .findAny()
            .orElseThrow(AuthenticationException::new);
    }
}
