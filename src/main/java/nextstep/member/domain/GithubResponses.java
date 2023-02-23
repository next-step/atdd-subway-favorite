package nextstep.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum GithubResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "admin@email.com"),

    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),

    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),

    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    public static GithubResponses ofCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 입니다. " + code));
    }

    public static GithubResponses ofAccessToken(String accessToken) {
        return Arrays.stream(values())
                .filter(e -> e.accessToken.equals(accessToken))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 입니다. " + accessToken));
    }
}
