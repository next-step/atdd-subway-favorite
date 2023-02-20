package nextstep.infra.github.mock;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.exception.InvalidGithubTokenException;

@RequiredArgsConstructor
@Getter
public enum GithubFakeResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    public static GithubFakeResponses fromCode(String code) {
        return Arrays.stream(values())
            .filter(it -> it.getCode().equals(code))
            .findFirst()
            .orElseThrow(InvalidGithubTokenException::new);
    }
}
