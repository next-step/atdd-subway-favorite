package nextstep.utils;

import java.util.Arrays;
import java.util.Optional;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(value = "test")
public enum GithubResponses {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com", 20),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com", 21),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com", 22),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com", 23);

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

    public static Optional<String> getAccessToken(String code) {
        return Arrays.stream(GithubResponses.values())
                .filter(githubResponse -> githubResponse.isSameAuthCode(code))
                .map(githubResponses -> githubResponses.accessToken)
                .findFirst();
    }

    public static Optional<GithubResponses> getGithubResponses(String token) {
        return Arrays.stream(GithubResponses.values())
                .filter(githubResponse -> githubResponse.isSameAccessToken(token))
                .findFirst();
    }

    private boolean isSameAccessToken(String token) {
        return this.accessToken.equals(token);
    }

    public boolean isSameAuthCode(String code) {
        return this.code.equals(code);
    }

    public GithubProfileResponse toGithubProfileResponse() {
        return new GithubProfileResponse(email, age);
    }
}
