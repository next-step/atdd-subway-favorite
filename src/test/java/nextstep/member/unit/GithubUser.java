package nextstep.member.unit;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum GithubUser {
    사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
    사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
    사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
    사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

    private final String code;
    private final String accessToken;
    private final String email;

    GithubUser(String code, String accessToken, String email) {
        this.code = code;
        this.accessToken = accessToken;
        this.email = email;
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

    private static final Map<String, GithubUser> cachedByCode =
            Stream.of(values()).collect(Collectors.toMap(GithubUser::getCode, v -> v));

    private static final Map<String, GithubUser> cachedByAccessToken =
            Stream.of(values()).collect(Collectors.toMap(GithubUser::getAccessToken, v -> v));

    public static GithubUser valueOfCode(String code) {
        return cachedByCode.get(code);
    }

    public static GithubUser valueOfAccessToken(String accessToken) {
        return cachedByAccessToken.get(accessToken);
    }
}
