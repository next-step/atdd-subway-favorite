package nextstep.utils;

import java.util.Arrays;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;

public enum GithubResponses {
    사용자1("code1", "access_token_1", "email1@email.com", 1),
    사용자2("code2", "access_token_2", "email2@email.com", 2),
    사용자3("code3", "access_token_3", "email3@email.com",3),
    사용자4("code4", "access_token_4", "email4@email.com", 4);

    private String code;
    private String accessToken;
    private String email;
    private Integer age;

    GithubResponses(String code, String accessToken, String email, Integer age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static GithubAccessTokenResponse getAccessToken(String code) {
        return Arrays.stream(values())
                .filter(response -> response.code.equals(code))
                .findAny()
                .map(response -> new GithubAccessTokenResponse(response.accessToken, null, null, null))
                .orElseThrow();
    }

    public static GithubProfileResponse getProfileFromAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(response -> response.equalAccessToken(accessToken))
                .findAny()
                .map(response -> new GithubProfileResponse(response.email, response.age))
                .orElseThrow();
    }

    public String getCode() {
        return code;
    }

    private boolean equalAccessToken(String accessToken) {
        String parsedToken = accessToken.split(" ")[1];
        return this.accessToken.equals(parsedToken);
    }
}
