package nextstep.github.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum GithubResponses {
    사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
    사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
    사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
    사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com"),
    존재하지_않는_사용자("fakecode", "faketoken", "fake@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    GithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }

    public static Optional<GithubResponses> findByEmail(String email) {
        return findBy(githubResponses -> githubResponses.email.equals(email));
    }

    public static Optional<GithubResponses> findByCode(String code) {
        return findBy(githubResponses -> githubResponses.code.equals(code));
    }

    private static Optional<GithubResponses> findBy(Predicate<GithubResponses> equals) {
        return Arrays.stream(values())
                .filter(equals)
                .findAny();
    }

    public String getCode() {
        return code;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
