package nextstep.auth.fake;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum GithubUsers {
    사용자1("code1", "access_token_1", "email1@email.com"),
    사용자2("code2", "access_token_2", "email2@email.com"),
    사용자3("code3", "access_token_3", "email3@email.com"),
    사용자4("code4", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    public static GithubUsers findByCode(String code) {
        return Arrays.stream(GithubUsers.values())
                .filter(it -> it.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
    }

    public static GithubUsers findByAccessToken(String accessToken) {
        return Arrays.stream(GithubUsers.values())
                .filter(it -> it.accessToken.equals(accessToken))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
    }
}
