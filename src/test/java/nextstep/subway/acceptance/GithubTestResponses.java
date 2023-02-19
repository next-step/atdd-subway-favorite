package nextstep.subway.acceptance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.exception.InvalidGithubTokenException;

@RequiredArgsConstructor
@Getter
public enum GithubTestResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    public static List<String> codes() {
        return Arrays.stream(values()).map(GithubTestResponses::getCode).collect(Collectors.toList());
    }

    public static String accessTokenFromCode(String code) {
        return Arrays.stream(values())
            .filter(it -> it.getCode().equals(code))
            .findFirst()
            .map(GithubTestResponses::getAccessToken)
            .orElseThrow(InvalidGithubTokenException::new);
    }

    public static GithubTestResponses of(String accessToken) {
        return Arrays.stream(values())
            .filter(it -> it.getAccessToken().equals(accessToken))
            .findFirst()
            .orElseThrow(InvalidGithubTokenException::new);
    }
}
