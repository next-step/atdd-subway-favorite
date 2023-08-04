package nextstep.auth.acceptance.utils;


import nextstep.auth.oauth2.github.dto.GithubAccessTokenResponse;
import nextstep.auth.oauth2.github.dto.GithubProfileResponse;
import nextstep.global.error.code.ErrorCode;
import nextstep.global.error.exception.AuthenticationException;

import java.util.Arrays;

public enum GithubMockUser {

    김민지("minji", "access_token_minji", "mjkim@test.com", 19),

    팜하니("hanni", "access_token_hanni", "hnpham@test.com", 18),

    다니엘("danielle", "access_token_danielle", "dnemarsh@test.com", 18),

    강해린("haerin", "access_token_haerin", "hrkang@test.com", 17),

    이혜인("hyein", "access_token_hyein", "hilee@test.com", 15);

    private final String code;

    private final String accessToken;

    private final String email;

    private final Integer age;

    GithubMockUser(String code,
                   String accessToken,
                   String email,
                   Integer age) {
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

    public Integer getAge() {
        return age;
    }

    public static GithubMockUser getUserByCode(String code) {
        return Arrays.stream(values())
                .filter(user -> user.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_GITHUB_CODE));
    }

    public static GithubMockUser getUserByAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(user -> String.format("token %s", user.getAccessToken()).equals(accessToken))
                .findAny()
                .orElseThrow(() -> new AuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));
    }

    public GithubAccessTokenResponse toGithubAccessTokenResponse() {
        return GithubAccessTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public GithubProfileResponse toGithubProfileResponse() {
        return GithubProfileResponse.builder()
                .email(email)
                .age(age)
                .build();
    }
}
