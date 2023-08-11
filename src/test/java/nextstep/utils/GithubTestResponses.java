package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;

import java.util.Arrays;

public enum GithubTestResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 10),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 20),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 30),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 40);

    private final String code;
    private final String accessToken;
    private final String email;
    private final int age;

    GithubTestResponses(String code, String accessToken, String email, int age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static GithubAccessTokenResponse createAccessToken(String code) {
        GithubTestResponses test = Arrays.stream(values())
                .filter(response -> response.code.equals(code))
                .findFirst()
                .orElseThrow();
        return new GithubAccessTokenResponse(test.accessToken);
    }

    public static GithubProfileResponse createGithubProfile(String accessToken) {
        GithubTestResponses test = Arrays.stream(values())
                .filter(response -> response.accessToken.equals(accessToken))
                .findFirst()
                .orElseThrow();
        return new GithubProfileResponse(test.email, test.age);
    }

    public String getCode() {
        return code;
    }
}
