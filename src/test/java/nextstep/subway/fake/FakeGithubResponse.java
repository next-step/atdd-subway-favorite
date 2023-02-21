package nextstep.subway.fake;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.exception.UnauthorizedException;

import java.util.Arrays;

public enum FakeGithubResponse {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private String code;
    private String accessToken;
    private String email;

    FakeGithubResponse(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    static String getAccessToken(String code) {
        return Arrays.stream(values())
                .filter(it -> it.code.equals(code))
                .findFirst()
                .map(it -> it.accessToken)
                .orElseThrow(() -> new UnauthorizedException("잘못된 코드 정보입니다"));
    }

    static GithubProfileResponse getProfile(String accessToken) {
        return Arrays.stream(values())
                .filter(it -> it.accessToken.equals(accessToken))
                .findFirst()
                .map(it -> new GithubProfileResponse(it.email))
                .orElseThrow(() -> new UnauthorizedException("잘못된 토큰 정보입니다"));
    }
}
