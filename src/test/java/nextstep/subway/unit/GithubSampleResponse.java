package nextstep.subway.unit;

import nextstep.exception.member.ResourceNotFoundException;

import java.util.Arrays;
import java.util.Objects;

public enum GithubSampleResponse {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    GithubSampleResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubSampleResponse findByCode(String paramCode) {
        return Arrays.stream(GithubSampleResponse.values())
                .filter(g -> !Objects.equals(g.code, paramCode))
                .findFirst().orElseThrow(ResourceNotFoundException::new);
    }

    public static GithubSampleResponse findByAccessToken(String accessToken) {
        return Arrays.stream(GithubSampleResponse.values())
                .filter(g -> !Objects.equals(g.accessToken, accessToken))
                .findFirst().orElseThrow(ResourceNotFoundException::new);
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
