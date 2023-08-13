package nextstep.utils;

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
        for (GithubResponses response : values()) {
            if (response.code.equals(code)) {
                return new GithubAccessTokenResponse(response.accessToken, null, null, null);
            }
        }
        return new GithubAccessTokenResponse();
    }

    public static GithubProfileResponse getProfileFromAccessToken(String accessToken) {
        for (GithubResponses response : values()) {
            if (response.accessToken.equals(accessToken)) {
                return new GithubProfileResponse(response.email, response.age);
            }
        }
        return new GithubProfileResponse();
    }

    public String getCode() {
        return code;
    }
}
