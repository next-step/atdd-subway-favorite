package nextstep.auth.acceptance.fake;

import nextstep.auth.dto.GithubProfileResponse;
import nextstep.auth.exception.AuthenticationException;

import java.util.Arrays;

public enum FakeGithubResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com"),
    비회원("abcdefghijk", "access_token_5", "email5@email.com");

    private String code;
    private String accessToken;
    private String email;

    FakeGithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
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

    public static String findAccessTokenByCode(String code) {
        return Arrays.stream(values())
                .filter(it -> it.code.equals(code))
                .findFirst()
                .map(it -> it.accessToken)
                .orElseThrow(AuthenticationException::new);
    }

    public static GithubProfileResponse findEmailByAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(it -> it.accessToken.equals(accessToken))
                .findFirst()
                .map(it -> new GithubProfileResponse(it.email))
                .orElseThrow(AuthenticationException::new);
    }
}
