package nextstep.auth.acceptance;

import java.util.Arrays;

public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 20),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 21),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 22),
    사용자_X("fm04fndkaladmd", "", "", 0);

    private final String code;
    private final String accessToken;
    private final String email;
    private final int age;

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

    public static String getAccessToken(String code) {
        return Arrays.stream(GithubResponses.values())
                .filter(member -> member.equalCode(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 요청입니다."))
                .getAccessToken();
    }

    public static GithubResponses getResponse(String accessToken) {
        return Arrays.stream(GithubResponses.values())
                .filter(member -> member.equalAccessToken(accessToken))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 요청입니다."));
    }

    private boolean equalCode(String code) {
        return this.code.equals(code);
    }
    private boolean equalAccessToken(String accessToken) {
        return this.accessToken.equals(accessToken);
    }
}