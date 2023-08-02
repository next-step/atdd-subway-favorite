package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;

public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 20),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 21),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 22),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 23);

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

    public static GithubResponses findByCode(String code) {
        for (GithubResponses githubResponses : values()) {
            if (githubResponses.code.equals(code)) {
                return githubResponses;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 code 입니다.");
    }

    public GithubAccessTokenResponse toGithubAccessTokenResponse() {
        return new GithubAccessTokenResponse(accessToken, "", "", "");
    }

    public static GithubResponses findByAccessToken(String accessToken) {
        String[] strings = accessToken.split(" ");
        for (GithubResponses githubResponses : values()) {
            if (githubResponses.accessToken.equals(strings[1])) {
                return githubResponses;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 accessToken 입니다.");
    }

    public GithubProfileResponse toGithubProfileResponse() {
        return new GithubProfileResponse(email, age);
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
