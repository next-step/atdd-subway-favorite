package nextstep.auth.token.acceptance;

import java.util.Arrays;
import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Git;

public enum GithubResponses {
    사용자1("code1", "accessToken1", "email1@email.com", 20),
    사용자2("code2", "accessToken2", "email2@email.com", 25),
    사용자3("code3", "accessToken3", "email3@email.com", 27);

    private String code;
    private String accessToken;
    private String email;
    private int age;

    GithubResponses(String code, String accessToken, String email, int age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static GithubResponses fromCode(String code) {
        return Arrays.stream(values())
            .filter(it -> it.code.equals(code))
            .findFirst()
            .orElseThrow();
    }

    public static GithubResponses fromAccessToken(String accessToken) {
        return Arrays.stream(values())
            .filter(it -> it.accessToken.equals(accessToken))
            .findFirst()
            .orElseThrow();
    }

    public GithubAccessTokenResponse toGithubAccessTokenResponse() {
        return new GithubAccessTokenResponse(this.accessToken, "bearer", "repo,gist", "bearer");
    }

    public GithubProfileResponse toGithubProfileResponse() {
        return new GithubProfileResponse(this.email, this.age);
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
