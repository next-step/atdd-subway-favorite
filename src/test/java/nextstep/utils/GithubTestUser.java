package nextstep.utils;

import java.util.Arrays;

public enum GithubTestUser {

    USER1("code1", "accessToken1", "user1@nextstep.com", 15),
    USER2("code2", "accessToken2", "user2@nextstep.com", 6),
    USER3("code3", "accessToken3", "user3@nextstep.com", 32),
    ;

    private String code;
    private String accessToken;
    private String email;
    private int age;

    GithubTestUser(String code, String accessToken, String email, int age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static GithubTestUser findUserByCode(String code) {
        return Arrays.stream(GithubTestUser.values())
                .filter(githubTestUser -> githubTestUser.sameCode(code))
                .findAny()
                .orElse(null);
    }

    public static GithubTestUser findUserByAccessToken(String accessToken) {
        return Arrays.stream(GithubTestUser.values())
                .filter(githubTestUser -> githubTestUser.sameAccessToken(accessToken))
                .findAny()
                .orElse(null);
    }

    private boolean sameCode(String code) {
        return this.code.equals(code);
    }

    private boolean sameAccessToken(String accessToken) {
        return this.accessToken.equals(accessToken.split(" ")[1]);
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

}
