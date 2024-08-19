package nextstep.utils;

import java.util.Arrays;

public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", "1234", 20),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", "5678", 19),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", "1q2w", 40),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", "qwer", 14);

    private String code;
    private String accessToken;
    private String email;
    private String password;
    private int age;

    GithubResponses(String code, String accessToken, String email, String password, int age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static String lookUpAccessToken(String code) {
        return Arrays.stream(values())
                .filter(response -> response.code.equals(code))
                .findFirst()
                .map(GithubResponses::getAccessToken)
                .orElseThrow(IllegalArgumentException::new);
    }

    public static GithubResponses lookUp(String accessToken) {
        return Arrays.stream(values())
                .filter(response -> response.accessToken.equals(accessToken))
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

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }
}