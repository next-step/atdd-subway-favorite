package nextstep.github.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum GithubResponses {
    사용자1("832ovnq039hfjn", "github_access_token_1", "email1@email.com", 20),
    사용자2("mkfo0aFa03m", "github_access_token_2", "email2@email.com", 30),
    사용자3("m-a3hnfnoew92", "github_access_token_3", "email3@email.com", 16),
    사용자4("nvci383mciq0oq", "github_access_token_4", "email4@email.com", 77),
    존재하지_않는_사용자("fakecode", "", "", 0);

    private final String code;
    private final String accessToken;
    private final String email;
    private final int age;

    GithubResponses(String code, String accessToken, String email, int age) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
        this.age = age;
    }

    public static Optional<GithubResponses> findByAccessToken(String accessToken) {
        return findBy(githubResponses -> githubResponses.accessToken.equals(accessToken));
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

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}
