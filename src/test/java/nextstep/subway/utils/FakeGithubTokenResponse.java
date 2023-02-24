package nextstep.subway.utils;

import nextstep.member.domain.exception.InvalidTokenException;

import java.util.Arrays;

public enum FakeGithubTokenResponse {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    FakeGithubTokenResponse(final String code, final String accessToken, final String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static String getTokenByCode(final String code) {
        return Arrays.stream(values())
                .filter(githubTokenResponse -> githubTokenResponse.getCode().equals(code))
                .findAny()
                .orElseThrow(InvalidTokenException::new)
                .getAccessToken();
    }

    public static String getEmailByToken(final String accessToken) {
        return Arrays.stream(values())
                .filter(githubTokenResponse -> githubTokenResponse.getAccessToken().equals(accessToken))
                .findAny()
                .orElseThrow(InvalidTokenException::new)
                .getEmail();
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
}
