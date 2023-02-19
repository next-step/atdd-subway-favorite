package nextstep.subway.config;

import nextstep.member.application.dto.GithubResponse;

import java.util.Arrays;

public enum MockGithubResponse implements GithubResponse {

    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;


    MockGithubResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static GithubResponse parse(String code) {
        return Arrays.stream(values())
                .filter(it -> it.code.equals(code))
                .findFirst()
                .get();
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public String getEmail() {
        return this.email;
    }
}
