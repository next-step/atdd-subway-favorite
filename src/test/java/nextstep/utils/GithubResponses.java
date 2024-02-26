package nextstep.utils;

import java.util.Arrays;
import java.util.Optional;

public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private final String code;
    private final String githubToken;
    private final String email;

    GithubResponses(String code, String githubToken, String email) {
        this.code = code;
        this.githubToken = githubToken;
        this.email = email;
    }

    public static Optional<String> findAccessToken(String code) {
        return Arrays.stream(values())
                .filter(githubResponses -> githubResponses.code.equalsIgnoreCase(code))
                .findFirst()
                .map(GithubResponses::getGithubToken);
    }

    public static Optional<String> findEmail(String githubToken) {
        return Arrays.stream(values())
                .filter(githubResponses -> githubResponses.githubToken.equalsIgnoreCase(githubToken))
                .findFirst()
                .map(GithubResponses::getEmail);
    }

    public String getCode() {
        return code;
    }

    public String getGithubToken() {
        return githubToken;
    }

    public String getEmail() {
        return email;
    }
}
