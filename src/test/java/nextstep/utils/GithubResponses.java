package nextstep.utils;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum GithubResponses {

    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    GithubResponses(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
    }
    
    public String getAccessToken() {
        return this.accessToken;
    }

    public String getEmail() {
        return this.email;
    }

    public static String findAccessTokenByCode(String code) {
        Predicate<GithubResponses> predicate = githubResponse -> githubResponse.code.equals(code);
        return findFilteredValue(predicate, GithubResponses::getAccessToken);
    }
    
    public static String findEmailByAccessToken(String accessToken) {
        Predicate<GithubResponses> predicate = githubResponse -> githubResponse.accessToken.equals(accessToken);
        return findFilteredValue(predicate, GithubResponses::getEmail);
    }

    private static String findFilteredValue(Predicate<GithubResponses> predicate, Function<GithubResponses, String> function) {
        return Arrays.stream(values())
                .filter(predicate)
                .map(function)
                .findFirst().orElseThrow();
    }
}
