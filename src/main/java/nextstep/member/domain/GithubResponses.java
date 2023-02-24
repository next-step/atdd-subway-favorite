package nextstep.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum GithubResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "admin@email.com");

    public static final String NOT_FOUND_USER_MESSAGE = "사용자를 찾을 수 없습니다.";
    private final String code;
    private final String accessToken;
    private final String email;

    public static GithubResponses ofCode(String code) {
        return findUser(e -> e.code.equals(code));
    }

    public static GithubResponses ofAccessToken(String accessToken) {
        return findUser(e -> e.accessToken.equals(accessToken));
    }

    private static GithubResponses findUser(Predicate<GithubResponses> predicate) {
        return Arrays.stream(values())
                .filter(predicate)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_USER_MESSAGE));
    }
}
