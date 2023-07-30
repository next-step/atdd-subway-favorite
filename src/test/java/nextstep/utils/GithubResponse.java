package nextstep.utils;

import java.util.Arrays;

public enum GithubResponse {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 12), 사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 24), 사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 32), 사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 45), 잘못된_토큰("asdfl892e23dss", "wrong-token", null, 0);

    private String code;
    private String accessToken;
    private String email;
    private int age;

    GithubResponse(String code, String accessToken, String email, int age) {
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

    public static GithubResponse fromCode(String code) {
        return Arrays.stream(values())
                .filter(response -> response.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("code가 잘못되었습니다."));
    }

    public static GithubResponse fromToken(String accessToken) {
        return Arrays.stream(values())
                .filter(response -> response.getAccessToken().equals(accessToken))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("accessToken이 잘못되었습니다."));
    }
}
